package esp8266;

import com.sun.squawk.*;
import com.sun.squawk.io.NetUtil;
import java.util.*;
import java.io.IOException;

public class Wifi {
	public final static byte NULL_MODE = 0;
	public final static byte STATION_MODE = 0x01;
	public final static byte SOFTAP_MODE = 0x02;
	public final static byte STATIONAP_MODE = 0x03;
	
	public final static int STATION_IF = 0;
	public final static int SOFTAP_IF = 1;
	
	public static native byte get_opmode();
	public static native byte get_opmode_default();
	public static native boolean set_opmode(byte mode);
	public static native boolean set_opmode_current(byte mode);

	public static class Station {
		
		public static StationConfig get_config() throws WifiException {
			return get_config(false);
		}
		
		public static StationConfig get_config(boolean isDefault) throws WifiException {
			StationConfig c = new StationConfig();
			boolean result;
			if (isDefault) {
				result = station_get_config(c);
			} else {
				result = station_get_config_default(c);
			}
			if (result) {
				return c;
			} else {
				throw new WifiException();
			}
		}

		public static void set_config(String ssid, String password, String bssid) throws WifiException {
			if (!station_set_config(new StationConfig(ssid, password, bssid))) {
				throw new WifiException();
			}
		}
		
		public static void set_config_current(String ssid, String password, String bssid) throws WifiException {
			if (!station_set_config_current(new StationConfig(ssid, password, bssid))) {
				throw new WifiException();
			}
		}

		public static List<BSS_Info> scan() throws IOException, InterruptedException {
			station_scan(new ScanConfig());
			System.out.println("waiting for scan done event");
			VMThread th = VMThread.currentThread();
			VM.waitForInterrupt(Events.WIFI_SCAN_DONE_EVENT);
			BSS_Info_Event evt = new BSS_Info_Event();
			receive_event(th.event, evt);
			List<BSS_Info> info = evt.parseAll();
			evt.dispose();
			return info;
		}

		public static StaMode_Connected_Event connect() throws IOException, InterruptedException {
			station_connect();
			System.out.println("connecting...");
			VMThread th = VMThread.currentThread();
			VM.waitForInterrupt(Events.WIFI_STAMODE_CONNECTED_EVENT);
			StaMode_Connected_Event evt = new StaMode_Connected_Event();
			receive_event(th.event, evt);
			return evt;
		}

		public static StaMode_Got_IP_Event getIP() throws IOException, InterruptedException {
			System.out.println("geting IP address...");
			VMThread th = VMThread.currentThread();
			VM.waitForInterrupt(Events.WIFI_STAMODE_GOT_IP_EVENT);
			StaMode_Got_IP_Event evt = new StaMode_Got_IP_Event();
			receive_event(th.event, evt);
			return evt;
		}
		
	}
	static native boolean station_get_config(StationConfig c);
	static native boolean station_get_config_default(StationConfig c);
	static native boolean station_set_config(StationConfig c);
	static native boolean station_set_config_current(StationConfig c);
	
	public static native int station_set_cert_key(byte[] client_cert, int client_cert_len,
												  byte[] private_key, int private_key_len, String private_key_passwd);
	public static native void station_clear_cert_key();
	public static native int station_set_username(String name);
	public static native void station_clear_username();
	public static native boolean station_connect();
	public static native boolean station_disconnect();
	public static native byte station_get_connect_status();
	public static native void station_scan(ScanConfig c);
	public static native boolean station_ap_number_set(byte ap_number);
	public static native byte station_get_ap_info(StationConfig[] c);
	public static native byte station_ap_change(byte ap_id);
	public static native byte station_get_current_ap_id();
	public static native byte station_get_auto_connect();
	public static native boolean station_set_auto_connect(byte b);
	public static native boolean station_dhcpc_start();
	public static native boolean station_dhcpc_stop();
	public static native int station_dhcpc_status();
	public static native boolean station_dhcpc_set_maxtry(int max);
	public static native boolean station_set_reconnect_policy(boolean b);
	public static native int station_get_rssi();
	public static native boolean station_set_hostname(String name);
	public static native int station_get_hostname(byte[] name);
	public static native boolean softap_get_config(SoftApConfig c);
	public static native boolean softap_get_config_default(SoftApConfig c);
	public static native boolean softap_set_config(SoftApConfig c);
	public static native boolean softap_set_config_current(SoftApConfig c);
	public static native byte softap_get_station_num();
	public static native void softap_get_station_info(StationInfo info);
	public static native boolean softap_dhcps_start();
	public static native boolean softap_dhcps_stop();
	public static native boolean softap_set_dhcps_lease(DHCPLease lease);
	public static native boolean softap_get_dhcps_lease(DHCPLease lease);
	public static native boolean softap_set_dhcps_lease_time(int minute);
	public static native int softap_get_dhcps_lease_time();
	public static native int softap_dhcps_status();
	public static native boolean softap_set_dhcps_offer_option(byte level, int optarg);
	public static native boolean set_phy_mode(int mode);
	public static native int get_phy_mode();
	public static native boolean get_ip_info(int index, IPInfo info);
	public static native boolean set_ip_info(int if_index, IPInfo info);
	public static native boolean set_macaddr(int if_index, byte[] macaddr);
	public static native boolean get_macaddr(int if_index, byte[] macaddr);
	public static native boolean set_sleep_type(int type);
	public static native int get_sleep_type();
	public static native void status_led_install (int gpio_id, int gpio_name, int gpio_func);
	public static native void status_led_uninstall();
	public static native boolean set_broadcast_if(int intrf);
	public static native int get_broadcast_if();
	public static native boolean wps_enable(int wps_type);
	public static native boolean wps_disable();
	public static native boolean wps_start();
	public static native int send_pkt_freedom(byte[] buf, int len, boolean sys_seq);
	public static native int rfid_locp_recv_open();
	public static native void rfid_locp_recv_close();
	public static native void enable_gpio_wakeup(int i, int intr_status);
	public static native void disable_gpio_wakeup();
	static native int wifi_get_event_id(int addr);
	static native void receive_event(int addr, Event evt);
	static native void dispose_scan_done_event(int rawData);
	
	public static class StationConfig {
		byte ssid[] = null;
		byte password[] = null;
		byte bssid_set = (byte)0;
		byte bssid[] = null;
		
		public StationConfig() {
			ssid = new byte[32];
			password = new byte[64];
			bssid_set = 0;
			bssid = new byte[6];
		}
		
		StationConfig(String ssid, String password, String bssid) {
			this.ssid = ssid.getBytes();
			this.password = password.getBytes();
			if (bssid == null) {
				this.bssid = null;
				this.bssid_set = (byte)0;
			} else {
				this.bssid = bssid.getBytes();
				this.bssid_set = (byte)1;
			}
		}
		
		public String toString() {
			return "ssid: " + NetUtil.formatByteArray(ssid) + ", password:" + new String(password) + (bssid_set != 0 ? (", bssid:" + NetUtil.formatByteArray(bssid)) : "");
		}
	}

	public static class SoftApConfig {
		public byte[] ssid = null;
		public byte[] password = null;
		public int ssid_len = 0;
		public int channel = 0;
		public int authmode = 0;
		public int ssid_hidden = 0;
		public int max_connection = 0;
		public int beacon_interval = 0;
	}

	public static class StationInfo {
		public byte[] bssid = null;
		public int ip = 0;

		public String toString() {
			return "bssid:" + NetUtil.formatByteArray(bssid) + ", ip_addr: " + NetUtil.formatIPAddr(ip);
		}
	}

	public static class DHCPLease {
		public boolean enable = false;
		public int start_ip = 0;
		public int end_ip = 0;
	}
	
	public static class ScanConfig {
		public byte[] ssid = null;
		public byte[] bssid = null;
		public byte channel = 0;
		public byte show_hidden = 0;
	}

	public static class IPInfo {
		public int ip = 0;
		public int netmask = 0;
		public int gw = 0;
	}

	public static interface Event {	}

	public static class BSS_Info_Event implements Event {
		int n = 0;
		int rawData = 0;

		static BSS_Info parse(int addr) {
			Address a = Address.fromPrimitive(addr);
			byte[] bssid = new byte[6];
			Unsafe.getBytes(a, 4, bssid, 0, 6);
			int ssid_len = Unsafe.getUByte(a, 42);
			byte[] ssid = new byte[ssid_len];
			Unsafe.getBytes(a, 10, ssid, 0, ssid_len);
			int channel = Unsafe.getByte(a, 43);
			int rssi = Unsafe.getByte(a, 44);
			int authmode = Unsafe.getUByte(a, 48);
			int is_hidden = Unsafe.getUByte(a, 52);
			int freq_offset = Unsafe.getUByte(a, 54);
			int freqcal_val = Unsafe.getUByte(a, 56);
			int esp_mesh_ie = Unsafe.getUByte(a, 60);
			return new BSS_Info(ssid, bssid, (byte)channel, (byte)rssi, (byte)authmode, (byte)is_hidden, (short)freq_offset, (short)freqcal_val, esp_mesh_ie);
		}
		
		List<BSS_Info> parseAll() {
			if (rawData == 0) {
				throw new NullPointerException();
			}
			List<BSS_Info> info = new ArrayList<BSS_Info>();
			for (int i = 0; i < n; i++) {
				info.add(parse(rawData));
				rawData += 64;
			}
			return info;
		}
		
		void dispose() {
			dispose_scan_done_event(rawData);
		}
	}

	public static class BSS_Info {
		public byte ssid[];
		public byte bssid[];
		public byte channel;
		public byte rssi;
		public byte authmode;
		public byte is_hidden;
		public short freq_offset;
		public short freqcal_val;
		public int esp_mesh_ie;

		BSS_Info(byte[] ssid, byte[] bssid, byte channel,
				 byte rssi, byte authmode, byte is_hidden,
				 short freq_offset, short freqcal_val, int esp_mesh_ie) {
			this.ssid = ssid;
			this.bssid = bssid;
			this.channel = channel;
			this.rssi = rssi;
			this.authmode = authmode;
			this.is_hidden = is_hidden;
			this.freq_offset = freq_offset;
			this.freqcal_val = freqcal_val;
			this.esp_mesh_ie = esp_mesh_ie;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("ssid: ");
			sb.append(new String(ssid));
			sb.append(", bssid: ");
			sb.append(NetUtil.formatByteArray(bssid));
			sb.append(", channel: " + channel);
			sb.append(", rssi: " + rssi);
			if (authmode >= 0 && authmode < authmode_name.length) {
				sb.append(", " + authmode_name[authmode]);
			}
			if (is_hidden != 0) {
				sb.append(", hidden");
			}
			sb.append(", freq_offset: " + freq_offset);
			sb.append(", freqcal_val: " + freqcal_val);
			sb.append(", esp_mesh_ie: " + esp_mesh_ie);
			return sb.toString();
		}
		final static String authmode_name[] = {
			"OPEN", "WEP", "WPA_PSK", "WPA2_PSK", "WPA_WPA2_PSK", "MAX"
		};
	}
	
	public static class StaMode_Connected_Event implements Event {
		public byte ssid[] = new byte[32];
		public byte ssid_len = 0;
		public byte bssid[] = new byte[6];
		public byte channel = 0;
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("ssid: ");
			sb.append(new String(ssid, 0, ssid_len));
			sb.append(", bssid: ");
			sb.append(NetUtil.formatByteArray(bssid));
			sb.append(", channel: " + channel);
			return sb.toString();
		}
	}
	
	public static class StaMode_Got_IP_Event implements Event {
		public int ip = 0;
		public int mask = 0;
		public int gw = 0;
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(NetUtil.formatIPAddr(ip));
			sb.append(',');
			sb.append(NetUtil.formatIPAddr(mask));
			sb.append(',');
			sb.append(NetUtil.formatIPAddr(gw));
			return sb.toString();
		}
	}
	
	public static class SoftAPMode_StaConnected_Event implements Event {
		public byte[] mac = new byte[6];
		public byte aid = 0;
	}
	
	static Class[] eventClasses = {BSS_Info_Event.class,
								  StaMode_Connected_Event.class,
								  StaMode_Got_IP_Event.class,
								  SoftAPMode_StaConnected_Event.class};
}

set_target_properties( squawk
     PROPERTIES COMPILE_FLAGS "-DMACROIZE -D_GNU_SOURCE -DSQUAWK_64=false  -DPLATFORM_BIG_ENDIAN=false -DPLATFORM_UNALIGNED_LOADS=false  -DNRF51 -DDEFAULT_RAM_SIZE=13*1024 -DMAIN_CLASS_NAME=com.sun.squawk.Dine -DPLATFORM_TYPE_BARE_METAL=1")

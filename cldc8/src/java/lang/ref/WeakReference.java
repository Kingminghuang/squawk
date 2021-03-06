/*
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * only, as published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included in the LICENSE file that accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 16 Network Circle, Menlo
 * Park, CA 94025 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package java.lang.ref;

/**
 * This class provides support for weak references. Weak references
 * are most often used to implement canonicalizing mappings.
 *
 * Suppose that the garbage collector determines at a certain
 * point in time that an object is weakly reachable.  At that
 * time it will atomically clear all the weak references to
 * that object and all weak references to any other weakly-
 * reachable objects from which that object is reachable
 * through a chain of strong and weak references.
 *
 * @version  12/19/01 (CLDC 1.1)
 * @since    JDK1.2, CLDC 1.1
 */

/*
 * Implementation note: It is generally recommended that
 * you don't subclass the WeakReference class.  The garbage
 * collector treats weak references specially, and the
 * subclasses of class WeakReference might not be handled
 * correctly by the garbage collector.
 */

public class WeakReference<T> extends Reference<T> {

    /**
     * Creates a new weak reference that refers to the given object.
     */
    public WeakReference(T referent) {
        super(referent);
    }
	
    public WeakReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }
}


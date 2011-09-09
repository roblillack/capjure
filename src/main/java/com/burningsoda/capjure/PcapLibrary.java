package com.burningsoda.capjure;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

import java.nio.IntBuffer;

public class PcapLibrary implements Library {
    //public static final String JNA_LIBRARY_NAME = LibraryExtractor.getLibraryPath("PcapLibrary", true, pcap.PcapLibrary.class);
    public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("pcap");
    static {
        Native.register("pcap");
    }

    public static interface pcap_direction_t {
        public static final int PCAP_D_INOUT = (int) 0;
        public static final int PCAP_D_IN = (int) 1;
        public static final int PCAP_D_OUT = (int) 2;
    }

    public static final int PCAP_ERROR_PERM_DENIED = (int) -8;
    public static final int PCAP_ERROR = (int) -1;
    public static final int PCAP_VERSION_MAJOR = (int) 2;
    public static final int PCAP_ERRBUF_SIZE = (int) 256;
    public static final int PCAP_WARNING_PROMISC_NOTSUP = (int) 2;
    public static final int PCAP_ERROR_BREAK = (int) -2;
    public static final int PCAP_ERROR_RFMON_NOTSUP = (int) -6;
    public static final int PCAP_ERROR_NOT_ACTIVATED = (int) -3;
    public static final int PCAP_ERROR_IFACE_NOT_UP = (int) -9;
    public static final int PCAP_VERSION_MINOR = (int) 4;
    public static final int PCAP_IF_LOOPBACK = (int) 1;
    public static final int PCAP_ERROR_NOT_RFMON = (int) -7;
    public static final int PCAP_WARNING = (int) 1;
    public static final int PCAP_NETMASK_UNKNOWN = (int) -1;
    public static final int PCAP_ERROR_ACTIVATED = (int) -4;
    public static final int PCAP_ERROR_NO_SUCH_DEVICE = (int) -5;

    public static class pcap_pkthdr extends Structure {
        /// time stamp
        public PcapLibrary.timeval ts;
        /// length of portion present
        public int caplen;
        /// length this packet (off wire)
        public int len;

        public pcap_pkthdr() {
            super();
            initFieldOrder();
        }

        protected void initFieldOrder() {
            setFieldOrder(new java.lang.String[] { "ts", "caplen", "len" });
        }

        public pcap_pkthdr(PcapLibrary.timeval ts, int caplen, int len) {
            super();
            this.ts = ts;
            this.caplen = caplen;
            this.len = len;
            initFieldOrder();
        }

        public static class ByReference extends pcap_pkthdr implements Structure.ByReference {}
        public static class ByValue extends pcap_pkthdr implements Structure.ByValue {}
    }

    public static class pcap_stat extends Structure {
        /// number of packets received
        public int ps_recv;
        /// number of packets dropped
        public int ps_drop;
        /// drops by interface -- only supported on some platforms
        public int ps_ifdrop;

        public pcap_stat() {
            super();
            initFieldOrder();
        }

        protected void initFieldOrder() {
            setFieldOrder(new java.lang.String[] { "ps_recv", "ps_drop", "ps_ifdrop" });
        }

        public pcap_stat(int ps_recv, int ps_drop, int ps_ifdrop) {
            super();
            this.ps_recv = ps_recv;
            this.ps_drop = ps_drop;
            this.ps_ifdrop = ps_ifdrop;
            initFieldOrder();
        }

        public static class ByReference extends pcap_stat implements Structure.ByReference {}

        public static class ByValue extends pcap_stat implements Structure.ByValue {}
    }

    ;

    public static class pcap_if extends Structure {
        public PcapLibrary.pcap_if.ByReference next;
        /// name to hand to "pcap_open_live()"
        public String name;
        /// textual description of interface, or NULL
        public String description;
        public PcapLibrary.pcap_addr.ByReference addresses;
        /// PCAP_IF_ interface flags
        public int flags;

        public pcap_if() {
            super();
            initFieldOrder();
        }

        protected void initFieldOrder() {
            setFieldOrder(new String[]{ "next", "name", "description", "addresses", "flags" });
        }

        public pcap_if(PcapLibrary.pcap_if.ByReference next, String name, String description, PcapLibrary.pcap_addr.ByReference addresses, int flags) {
            super();
            this.next = next;
            this.name = name;
            this.description = description;
            this.addresses = addresses;
            this.flags = flags;
            initFieldOrder();
        }

        public static class ByReference extends pcap_if implements Structure.ByReference {}
        public static class ByValue extends pcap_if implements Structure.ByValue {}
    }

    public static class pcap_addr extends Structure {
        public PcapLibrary.pcap_addr.ByReference next;
        /// address
        public Pointer addr;
        /// netmask for that address
        public Pointer netmask;
        /// broadcast address for that address
        public Pointer broadaddr;
        /// P2P destination address for that address
        public Pointer dstaddr;

        public pcap_addr() {
            super();
            initFieldOrder();
        }

        protected void initFieldOrder() {
            setFieldOrder(new java.lang.String[]{"next", "addr", "netmask", "broadaddr", "dstaddr"});
        }

        public pcap_addr(PcapLibrary.pcap_addr.ByReference next, Pointer addr, Pointer netmask, Pointer broadaddr, Pointer dstaddr) {
            super();
            this.next = next;
            this.addr = addr;
            this.netmask = netmask;
            this.broadaddr = broadaddr;
            this.dstaddr = dstaddr;
            initFieldOrder();
        }

        public static class ByReference extends pcap_addr implements Structure.ByReference {

        }

        ;

        public static class ByValue extends pcap_addr implements Structure.ByValue {

        }

        ;
    }

    ;

    public interface pcap_handler extends Callback {
        void apply(Pointer u_charPtr1, PcapLibrary.pcap_pkthdr pcap_pkthdrPtr1, Pointer u_charPtr2);
    }

    ;

    @Deprecated
    public static native String pcap_lookupdev(Pointer charPtr1);

    public static native String pcap_lookupdev(String charPtr1);

    @Deprecated
    public static native int pcap_lookupnet(Pointer charPtr1, Pointer bpf_u_int32Ptr1, Pointer bpf_u_int32Ptr2, Pointer charPtr2);

    public static native int pcap_lookupnet(String charPtr1, Pointer bpf_u_int32Ptr1, Pointer bpf_u_int32Ptr2, String charPtr2);

    @Deprecated
    public static native Pointer pcap_create(Pointer charPtr1, Pointer charPtr2);

    public static native Pointer pcap_create(String charPtr1, String charPtr2);

    public static native int pcap_set_snaplen(Pointer pcap_tPtr1, int int1);

    public static native int pcap_set_promisc(Pointer pcap_tPtr1, int int1);

    public static native int pcap_can_set_rfmon(Pointer pcap_tPtr1);

    public static native int pcap_set_rfmon(Pointer pcap_tPtr1, int int1);

    public static native int pcap_set_timeout(Pointer pcap_tPtr1, int int1);

    public static native int pcap_set_buffer_size(Pointer pcap_tPtr1, int int1);

    public static native int pcap_activate(Pointer pcap_tPtr1);

    public static native Pointer pcap_open_live(String deviceName, int int1, int int2, int int3, PointerByReference errbuf);

    public static native Pointer pcap_open_dead(int int1, int int2);

    @Deprecated
    public static native Pointer pcap_open_offline(Pointer charPtr1, Pointer charPtr2);

    public static native Pointer pcap_open_offline(String charPtr1, String charPtr2);

    @Deprecated
    public static native Pointer pcap_fopen_offline(Pointer FILEPtr1, Pointer charPtr1);

    public static native Pointer pcap_fopen_offline(Pointer FILEPtr1, String charPtr1);

    public static native void pcap_close(Pointer pcap_tPtr1);

    public static native int pcap_loop(Pointer pcap_tPtr1, int int1, PcapLibrary.pcap_handler pcap_handler1, Pointer u_charPtr1);

    public static native int pcap_dispatch(Pointer pcap_tPtr1, int int1, PcapLibrary.pcap_handler pcap_handler1, Pointer u_charPtr1);

    public static native Pointer pcap_next(Pointer handle, PcapLibrary.pcap_pkthdr header);

    //@Deprecated
    //public static native int pcap_next_ex(Pointer pcap_tPtr1, PointerByReference pcap_pkthdrPtrPtr1, PointerByReference u_charPtrPtr1);

    //public static native int pcap_next_ex(Pointer pcap_tPtr1, PcapLibrary.pcap_pkthdr.ByReference pcap_pkthdrPtrPtr1[], PointerByReference u_charPtrPtr1);

    public static native void pcap_breakloop(Pointer pcap_tPtr1);

    public static native int pcap_stats(Pointer pcap_tPtr1, PcapLibrary.pcap_stat pcap_statPtr1);

    public static native int pcap_setfilter(Pointer pcap_tPtr1, Pointer bpf_programPtr1);

    public static native int pcap_setdirection(Pointer pcap_tPtr1, int pcap_direction_t1);

    public static native int pcap_getnonblock(Pointer pcap_tPtr1, String charPtr1);

    public static native int pcap_setnonblock(Pointer pcap_tPtr1, int int1, String charPtr1);

    //public static native int pcap_inject(Pointer pcap_tPtr1, Pointer voidPtr1, NativeSize size_t1);

    //public static native int pcap_sendpacket(Pointer pcap_tPtr1, Pointer u_charPtr1[], int int1);

    public static native String pcap_statustostr(int int1);

    public static native String pcap_strerror(int int1);

    public static native String pcap_geterr(Pointer pcap_tPtr1);

    public static native void pcap_perror(Pointer pcap_tPtr1, String charPtr1);

    public static native int pcap_compile(Pointer pcap_tPtr1, Pointer bpf_programPtr1, String charPtr1, int int1, int bpf_u_int321);

    public static native int pcap_compile_nopcap(int int1, int int2, Pointer bpf_programPtr1, String charPtr1, int int3, int bpf_u_int321);

    public static native void pcap_freecode(Pointer bpf_programPtr1);

    //public static native int pcap_offline_filter(Pointer bpf_programPtr1, PcapLibrary.pcap_pkthdr pcap_pkthdrPtr1, Pointer u_charPtr1[]);

    public static native int pcap_datalink(Pointer pcap_tPtr1);

    public static native int pcap_datalink_ext(Pointer pcap_tPtr1);

    public static native int pcap_list_datalinks(Pointer pcap_tPtr1, PointerByReference intPtrPtr1);

    public static native int pcap_set_datalink(Pointer pcap_tPtr1, int int1);

    public static native void pcap_free_datalinks(IntBuffer intPtr1);

    public static native int pcap_datalink_name_to_val(String charPtr1);

    public static native String pcap_datalink_val_to_name(int int1);

    public static native String pcap_datalink_val_to_description(int int1);

    public static native int pcap_snapshot(Pointer pcap_tPtr1);

    public static native int pcap_is_swapped(Pointer pcap_tPtr1);

    public static native int pcap_major_version(Pointer pcap_tPtr1);

    public static native int pcap_minor_version(Pointer pcap_tPtr1);

    public static native Pointer pcap_file(Pointer pcap_tPtr1);

    public static native int pcap_fileno(Pointer pcap_tPtr1);

    public static native Pointer pcap_dump_open(Pointer pcap_tPtr1, String charPtr1);

    public static native Pointer pcap_dump_fopen(Pointer pcap_tPtr1, Pointer fp);

    public static native Pointer pcap_dump_file(Pointer pcap_dumper_tPtr1);

    public static native int pcap_dump_ftell(Pointer pcap_dumper_tPtr1);

    public static native int pcap_dump_flush(Pointer pcap_dumper_tPtr1);

    public static native void pcap_dump_close(Pointer pcap_dumper_tPtr1);

    //public static native void pcap_dump(Pointer u_charPtr1, PcapLibrary.pcap_pkthdr pcap_pkthdrPtr1, Pointer u_charPtr2[]);

    //public static native int pcap_findalldevs(PcapLibrary.pcap_if.ByReference pcap_if_tPtrPtr1[], String charPtr1);

    //public static native int pcap_findalldevs(PcapLibrary.pcap_if.ByReference pcap_if_tPtrPtr1[], Pointer charPtr1);

    public static native void pcap_freealldevs(PcapLibrary.pcap_if pcap_if_tPtr1);

    public static native String pcap_lib_version();

    //public static native int bpf_filter(Pointer bpf_insnPtr1, Pointer u_charPtr1[], int u_int1, int u_int2);

    public static native int bpf_validate(Pointer f, int len);

    public static native String bpf_image(Pointer bpf_insnPtr1, int int1);

    public static native void bpf_dump(Pointer bpf_programPtr1, int int1);

    public static native int pcap_get_selectable_fd(Pointer pcap_tPtr1);

    /// Undefined type
    public static interface pcap_dumper {

    }

    /// Undefined type
    public static interface bpf_program {

    }

    /// Undefined type
    public static interface sockaddr {

    }

    ;

    /// Undefined type
    public static interface bpf_insn {

    }

    ;

    /// Undefined type
    public static interface FILE {

    }

    public static class timeval extends Structure {
        public long tv_sec;
        public long tv_usec;

        protected void initFieldOrder() {
            setFieldOrder(new String[] { "tv_sec", "tv_usec" });
        }

        public timeval() {
            super();
            initFieldOrder();
        }

        public timeval(long sec, long usec) {
            super();
            tv_sec = sec;
            tv_usec = usec;
        }
    }
}

package indi.tshoiasc.wenuc_http.exec;

import java.lang.management.ManagementFactory;
import java.util.Map;

import com.sun.management.OperatingSystemMXBean;
import indi.tshoiasc.wenuc_http.exec.storage.User;
import indi.tshoiasc.wenuc_http.exec.storage.UserInfo;

public class Status {
    private static OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static int cpuLoad() {
        double cpuLoad = osmxb.getSystemCpuLoad();
        return (int) (cpuLoad * 100);

    }

    public static int memoryLoad() {
        double totalvirtualMemory = osmxb.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();

        double value = freePhysicalMemorySize / totalvirtualMemory;
        return (int) ((1 - value) * 100);

    }


}

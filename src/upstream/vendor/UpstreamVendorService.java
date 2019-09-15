package upstream.vendor;

import annotation.Transactional;
import global.GlobalService;
import requestMapping.Service;

public class UpstreamVendorService {

    @Service
    GlobalService globalService;

    @Transactional
    public void addNewVendor(String name, long locationId) throws Exception {
        UpstreamVendor vendor = new UpstreamVendor();
        vendor.setVendorName(name);
        vendor.setVendorLocation(locationId);
        globalService.save(vendor);
    }




}

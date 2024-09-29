/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Administrator
 */
@javax.ws.rs.ApplicationPath("webresources")

public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
//        resources.add(ACS.AcsResource.class);
//        resources.add(ATM.AtmResource.class);
//        resources.add(ATMDownTime.ATMDownTimeResource.class);
////        resources.add(ATMFault.AtmFaultResource.class);
//        resources.add(BackEnd.GenericResource.class);
//        resources.add(Journal.JournalResource.class);
//        resources.add(NRFC.NRFCResource.class);
        resources.add(BackEnd.GenericResource.class);
        resources.add(Reports.ReportsResource.class);
        resources.add(Services.ServicesResource.class);
        resources.add(Users.UserResource.class);
    }
    
}

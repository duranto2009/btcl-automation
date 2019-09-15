package test.junit;

import common.ApplicationGroupType;
import global.GlobalService;
import nix.application.NIXApplication;
import nix.application.NIXApplicationService;
import nix.demandnote.NIXDemandNote;
import nix.demandnote.NIXDemandNoteService;
import org.junit.Test;

import org.mockito.Mock;
import util.ServiceDAOFactory;

import java.util.LinkedList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
public class MockitoPlayground {

    @Mock GlobalService globalService;
    @Mock
    NIXApplicationService nixApplicationService;

    @Mock
    NIXApplication nixApplication;

    @Test
    public void test1() {
        List list = mock(List.class);

        list.add("one");
        list.clear();

        verify(list).add("one");
        verify(list).clear();


    }

    @Test
    public void test2() {

        //You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        //following prints "first"
        System.out.println(mockedList.get(0));

        //following throws runtime exception
        System.out.println(mockedList.get(1));

        //following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed.
        verify(mockedList).get(0);
    }

    @Test
    public void nixDNAutofillTest() {



        try {
            NIXDemandNoteService nixDemandNoteService = ServiceDAOFactory.getService(NIXDemandNoteService.class);
            assertNotNull(nixDemandNoteService);

            long applicationId = mock(Long.class);
            long clientId = mock(Long.class);
            ApplicationGroupType applicationGroupType = mock(ApplicationGroupType.class);
            NIXApplicationService nixApplicationService = mock(NIXApplicationService.class);
            NIXApplication nixApplication = mock(NIXApplication.class);

            when(nixApplicationService.getApplicationById(applicationId)).thenReturn(nixApplication);
            when(nixApplication.getClient()).thenReturn(clientId);
            NIXDemandNote nixDemandNote = nixDemandNoteService.getDNAutofilled(applicationId, applicationGroupType);


//            assertEquals(, nixDemandNote.getNixPortCharge());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

package com.ikhramchenkov.staffmanagement;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.serverOrReceiverFault;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaffManagementApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @Before
    public void setUp() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void test01AddWorkerDepartmentSet() {
        StringSource stringSource = new StringSource("<AddWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <Worker>\n" +
                "                <FirstName>Нестафф</FirstName>\n" +
                "                <LastName>Неменеджерофф</LastName>\n" +
                "                <PhoneNumber>+7-999-HIRE-ME</PhoneNumber>\n" +
                "                <DepartmentId>1</DepartmentId>\n" +
                "            </Worker>\n" +
                "        </AddWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    @Test
    public void test02AddWorkerNoDepartmentSet() {
        StringSource stringSource = new StringSource("<AddWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <Worker>\n" +
                "                <FirstName>Стафф</FirstName>\n" +
                "                <LastName>Менеджерофф</LastName>\n" +
                "            </Worker>\n" +
                "        </AddWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    @Test
    public void test03AddWorkerDuplicateFault() {
        StringSource stringSource = new StringSource("<AddWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <Worker>\n" +
                "                <FirstName>Стафф</FirstName>\n" +
                "                <LastName>Менеджерофф</LastName>\n" +
                "            </Worker>\n" +
                "        </AddWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(serverOrReceiverFault());
    }

    @Test
    public void test04EditWorkerSuccess() {
        StringSource stringSource = new StringSource("<EditWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <Worker>\n" +
                "                <Id>2</Id>\n" +
                "                <FirstName>Иван</FirstName>\n" +
                "                <LastName>Васильевич</LastName>\n" +
                "                <PhoneNumber>+7-LOOK-AT-ME</PhoneNumber>\n" +
                "            </Worker>\n" +
                "        </EditWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    @Test
    public void test05EditWorkerDuplicateFault() {
        StringSource stringSource = new StringSource("<EditWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <Worker>\n" +
                "                <Id>2</Id>\n" +
                "                <FirstName>Стафф</FirstName>\n" +
                "                <LastName>Менеджерофф</LastName>\n" +
                "                <PhoneNumber>+7-999-IM-GOOD</PhoneNumber>\n" +
                "            </Worker>\n" +
                "        </EditWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    @Test
    public void test06DeleteWorkerFault() {
        StringSource stringSource = new StringSource("<DeleteWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <Id>231</Id>\n" +
                "        </DeleteWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(serverOrReceiverFault());
    }

    @Test
    public void test07DeleteWorkerSuccess() {
        StringSource stringSource = new StringSource("<DeleteWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <Id>2</Id>\n" +
                "        </DeleteWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    @Test
    public void test08AssignWorkerSuccess() {
        StringSource stringSource = new StringSource("<AssignWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <WorkerId>1</WorkerId>\n" +
                "            <DepartmentId>1</DepartmentId>\n" +
                "        </AssignWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    /**
     * Also proves that deleted worker in test7 actually deleted
     */
    @Test
    public void test09AssignWorkerDoesNotExistFault() {
        StringSource stringSource = new StringSource("<AssignWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <WorkerId>2</WorkerId>\n" +
                "            <DepartmentId>1</DepartmentId>\n" +
                "        </AssignWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(serverOrReceiverFault());
    }

    @Test
    public void test10UnassignWorkerSuccess() {
        StringSource stringSource = new StringSource("<UnassignWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <WorkerId>1</WorkerId>\n" +
                "        </UnassignWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    @Test
    public void test11UnassignWorkerAlreadyUnassignedFault() {
        StringSource stringSource = new StringSource("<UnassignWorkerRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <WorkerId>1</WorkerId>\n" +
                "        </UnassignWorkerRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(serverOrReceiverFault());
    }

    @Test
    public void test12GetStaffNoFilterSuccess() {
        StringSource stringSource = new StringSource("<GetStaffRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                        "        </GetStaffRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }

    @Test
    public void test13GetStaffFilteredSuccess() {
        StringSource stringSource = new StringSource("<GetStaffRequest xmlns=\"http://ikhramchenkov.com/staffmanagement/jaxb/\">\n" +
                "            <DepartmentId>1</DepartmentId>\n" +
                "        </GetStaffRequest>");
        mockClient.sendRequest(withPayload(stringSource)).andExpect(noFault());
    }
}

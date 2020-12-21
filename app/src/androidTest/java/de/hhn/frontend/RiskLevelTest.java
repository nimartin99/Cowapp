package de.hhn.frontend;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hhn.cowapp.datastorage.LocalSafer;
import de.hhn.cowapp.risklevel.DirectContact;
import de.hhn.cowapp.risklevel.IndirectContact;
import de.hhn.cowapp.risklevel.RiskLevel;
import de.hhn.cowapp.utils.DateHelper;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the RiskLevel class, and Local Safer Methods used by the Risk Level class
 *
 * @author jonas
 * @version 07.12.2020
 */

public class RiskLevelTest {

    Date outDatedTestDate = new GregorianCalendar(2020, Calendar.NOVEMBER, 4).getTime();
    Date notOutDatedTestDate = DateHelper.getCurrentDate();
    LocalDate localDate = LocalDate.now().plusDays(1);
    ZoneId defaultZoneId = ZoneId.systemDefault();
    Date notOutDatedTestDate1 = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());


    /**
     * Test of the risk level calculation by direct and indirect contacts.
     */
    @Test
    public void riskLevelCalculationTest() {

        RiskLevel.deleteAllContacts();

        RiskLevel.addContact(new IndirectContact(outDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(35, LocalSafer.getRiskLevel(null));

        RiskLevel.addContact(new IndirectContact(outDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(40, LocalSafer.getRiskLevel(null));

        RiskLevel.addContact(new IndirectContact(outDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(45, LocalSafer.getRiskLevel(null));

        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new IndirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(70, LocalSafer.getRiskLevel(null));


        RiskLevel.addContact(new DirectContact(outDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(70, LocalSafer.getRiskLevel(null));

        RiskLevel.addContact(new DirectContact(outDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(75, LocalSafer.getRiskLevel(null));

        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(95, LocalSafer.getRiskLevel(null));

        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(95, LocalSafer.getRiskLevel(null));

        RiskLevel.deleteAllContacts();
        RiskLevel.calculateRiskLevel();
        assertEquals(0, LocalSafer.getRiskLevel(null));

        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.addContact(new DirectContact(notOutDatedTestDate));
        RiskLevel.calculateRiskLevel();
        assertEquals(75, LocalSafer.getRiskLevel(null));

        RiskLevel.reportInfection();
        assertEquals(100, LocalSafer.getRiskLevel(null));

        RiskLevel.reportNegativeInfectionTestResult();
        assertEquals(0, LocalSafer.getRiskLevel(null));

    }

    /**
     * check if outdated contacts are removed from the contact list.
     */
    @Test
    public void removeOldContactsTest() {
        RiskLevel.deleteAllContacts();

        ArrayList<IndirectContact> indirectContactArrayList = LocalSafer.getListOfIndirectContacts(null);
        ArrayList<DirectContact> directContactArrayList = LocalSafer.getListOfDirectContacts(null);

        //add 5 Indirect contacts, 2 are outdated(older than 14 days), 3 are younger than 14 days
        indirectContactArrayList.add(new IndirectContact(notOutDatedTestDate));
        indirectContactArrayList.add(new IndirectContact(notOutDatedTestDate));
        indirectContactArrayList.add(new IndirectContact(notOutDatedTestDate));
        indirectContactArrayList.add(new IndirectContact(outDatedTestDate));
        indirectContactArrayList.add(new IndirectContact(outDatedTestDate));

        //add 5 Direct contacts, 2 are outdated(older than 14 days), 3 are younger than 14 days
        directContactArrayList.add(new DirectContact(outDatedTestDate));
        directContactArrayList.add(new DirectContact(notOutDatedTestDate));
        directContactArrayList.add(new DirectContact(outDatedTestDate));
        directContactArrayList.add(new DirectContact(notOutDatedTestDate));
        directContactArrayList.add(new DirectContact(notOutDatedTestDate));

        LocalSafer.safeListOfDirectContacts(directContactArrayList, null);
        LocalSafer.safeListOfIndirectContacts(indirectContactArrayList, null);

        RiskLevel.deleteOldContacts();

        assertEquals(3, LocalSafer.getListOfDirectContacts(null).size());
        assertEquals(3, LocalSafer.getListOfIndirectContacts(null).size());
    }

    /**
     * check if the methods to report a positive or negative infection report correspond to the expected risk level value
     */
    @Test
    public void InfectionStatusTest() {

        RiskLevel.reportInfection();
        System.out.println(LocalSafer.getRiskLevel(null));
        assertEquals(100, LocalSafer.getRiskLevel(null));
        RiskLevel.reportNegativeInfectionTestResult();
        assertEquals(0, LocalSafer.getRiskLevel(null));

    }

    /**
     * check if the a positive infection status gets resetted if the day of the reported infection is older than 14 days
     */
    @Test
    public void InfectionOutdatedTest() {
        RiskLevel.deleteAllContacts();
        RiskLevel.reportInfection();
        assertEquals(100, LocalSafer.getRiskLevel(null));


        LocalSafer.safeDateOfLastReportedInfection(DateHelper.convertDateToString(outDatedTestDate), null);
        RiskLevel.checkIfInfectionHasExpired();
        assertEquals(0, LocalSafer.getRiskLevel(null));
    }

    /**
     * generate lists containing some
     */
    @Test
    public void TestSaveAndGetContactArrayLists() {
        RiskLevel.deleteAllContacts();
        ArrayList<IndirectContact> indirectContactArrayList = new ArrayList<>();
        ArrayList<DirectContact> directContactArrayList = new ArrayList<>();

        indirectContactArrayList.add(new IndirectContact(notOutDatedTestDate));
        indirectContactArrayList.add(new IndirectContact(notOutDatedTestDate1));
        indirectContactArrayList.add(new IndirectContact(notOutDatedTestDate));

        LocalSafer.safeListOfIndirectContacts(indirectContactArrayList, null);

        ArrayList<IndirectContact> indirectContactArrayListByLocalSafer = LocalSafer.getListOfIndirectContacts(null);

        assertEquals(indirectContactArrayList.size(), indirectContactArrayListByLocalSafer.size());

        directContactArrayList.add(new DirectContact(notOutDatedTestDate));
        directContactArrayList.add(new DirectContact(notOutDatedTestDate1));
        directContactArrayList.add(new DirectContact(notOutDatedTestDate));
        directContactArrayList.add(new DirectContact(notOutDatedTestDate1));
        LocalSafer.safeListOfDirectContacts(directContactArrayList, null);
        ArrayList<DirectContact> directContactArrayListByLocalSafer = LocalSafer.getListOfDirectContacts(null);

        assertEquals(directContactArrayList.size(), directContactArrayListByLocalSafer.size());
        //first contact from the generated list
        DirectContact directContact1 = directContactArrayList.get(0);
        //first contact from the Local safer
        DirectContact directContact2 = directContactArrayListByLocalSafer.get(0);
        //second contact from the generated list
        DirectContact directContact3 = directContactArrayList.get(0);
        //second contact from the Local safer
        DirectContact directContact4 = directContactArrayListByLocalSafer.get(0);
        //third contact from the generated list
        DirectContact directContact5 = directContactArrayList.get(0);
        //third contact from the Local safer
        DirectContact directContact6 = directContactArrayListByLocalSafer.get(0);

        assertEquals(directContact1.getDate().toString(), directContact2.getDate().toString());
        assertEquals(directContact3.getDate().toString(), directContact4.getDate().toString());
        assertEquals(directContact5.getDate().toString(), directContact6.getDate().toString());

    }
}
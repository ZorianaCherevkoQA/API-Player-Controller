package api.utils;

import api.objects.SuperUser;
import org.aeonbits.owner.ConfigFactory;
import properties.TestInitValues;

import static api.enums.SuperUserRole.SUPERVISOR;


public class Base {

    public static final TestInitValues testProp = ConfigFactory.create(TestInitValues.class);

    public static final SuperUser supervisor = new SuperUser(testProp.SUPERVISOR_LOGIN(), SUPERVISOR.getRole());
}

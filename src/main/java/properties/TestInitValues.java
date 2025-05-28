package properties;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface TestInitValues extends Config {

    @Key("BASE_URL")
    String BASE_URL();

    @Key("supervisorLogin")
    String SUPERVISOR_LOGIN();

    @Key("consoleLog")
    Boolean consoleLog();
}

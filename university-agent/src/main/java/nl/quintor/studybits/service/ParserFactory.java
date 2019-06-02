package nl.quintor.studybits.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParserFactory {

    @Autowired
    OsirisParser osirisParser;

    public Parser getParser(String system) {
        if (system == null) {
            return null;
        }
        if (system.equalsIgnoreCase("Progress")) {
            return ProgressCreateService();
        }
        if (system.equalsIgnoreCase("Osiris")) {
            log.debug("Osiris parsen creëren");
            return OsirisCreateService();
        }
        return null;

    }

    public OsirisParser OsirisCreateService() {
        return osirisParser;
    }

    public OsirisParser ProgressCreateService() {
        return new OsirisParser("ProgressParser");
    }
}

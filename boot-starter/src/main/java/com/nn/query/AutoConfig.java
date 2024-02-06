package com.nn.query;

import com.nn.query.properties.DataSourceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

import java.util.logging.Logger;

/**
 * @author niann
 * @date 2024/2/6 21:45
 * @description
 **/

@AutoConfiguration
@Import(DataSourceProperties.class)
public class AutoConfig {


    private final Logger logger = Logger.getLogger("");

    public AutoConfig() {
        printLogo();
    }

    /**
     * 打印logo
     */
    public void printLogo() {
        logger.info("  ▄████▄    ▄█████▄  ▄▄█████▄  ▀██  ███             ▄███▄██  ██    ██   ▄████▄    ██▄████  ▀██  ███\n" +
                " ██▄▄▄▄██   ▀ ▄▄▄██  ██▄▄▄▄ ▀   ██▄ ██             ██▀  ▀██  ██    ██  ██▄▄▄▄██   ██▀       ██▄ ██\n" +
                " ██▀▀▀▀▀▀  ▄██▀▀▀██   ▀▀▀▀██▄    ████▀    █████    ██    ██  ██    ██  ██▀▀▀▀▀▀   ██         ████▀\n" +
                " ▀██▄▄▄▄█  ██▄▄▄███  █▄▄▄▄▄██     ███              ▀██▄▄███  ██▄▄▄███  ▀██▄▄▄▄█   ██          ███\n" +
                "   ▀▀▀▀▀    ▀▀▀▀ ▀▀   ▀▀▀▀▀▀      ██                 ▀▀▀ ██   ▀▀▀▀ ▀▀    ▀▀▀▀▀    ▀▀          ██\n" +
                "                                ███                      ██                                 ███\n" +
                "                                                                                             (v1.1)\n");
    }
}

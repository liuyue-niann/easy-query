package com.nn.query;

import com.nn.query.properties.DataSourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author niann
 * @date 2024/2/6 21:45
 * @description
 **/

@AutoConfiguration
@Import(DataSourceProperties.class)
public class AutoConfig {


    private final Logger logger = LoggerFactory.getLogger(AutoConfig.class);

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

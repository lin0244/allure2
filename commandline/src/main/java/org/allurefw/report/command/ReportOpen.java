package org.allurefw.report.command;

import io.airlift.airline.Command;
import io.airlift.airline.Option;
import io.airlift.airline.OptionType;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static org.allurefw.report.utils.CommandUtils.openBrowser;
import static org.allurefw.report.utils.CommandUtils.setUpServer;
import static org.allurefw.report.utils.CommandUtils.validateDirectoryExists;

/**
 * @author Artem Eroshenko <eroshenkoam@qameta.io>
 */
@Command(name = "open", description = "Open generated report")
public class ReportOpen extends ReportCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportOpen.class);

    @Option(name = {"-p", "--port"}, type = OptionType.COMMAND,
            description = "This port will be used to start web server for the report")
    protected int port = 0;

    @Override
    protected void runUnsafe() throws Exception {
        Path reportDirectory = getReportDirectoryPath();
        validateDirectoryExists(reportDirectory);
        LOGGER.info("Starting web server for the report directory <{}>", reportDirectory);
        Server server = setUpServer(port, reportDirectory);
        server.start();

        openBrowser(server.getURI());
        LOGGER.info("Server started at <{}>. Press <Ctrl+C> to exit ...", server.getURI());
        server.join();
    }
}
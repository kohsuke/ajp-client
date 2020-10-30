package org.kohsuke.ajp;

import org.apache.commons.cli.*;
import org.kohsuke.ajp.client.SimpleAjpClient;
import org.kohsuke.ajp.client.TesterAjpMessage;

import java.net.URL;

class Main {
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption("H","header", true, "add header");

    try {
      CommandLineParser clp = new DefaultParser();
      CommandLine cl = clp.parse(options, args);

      // create a message that indicates the beginning of the request

      System.out.println(cl.getArgs()[0]);
      URL url = new URL(cl.getArgs()[0]);
      SimpleAjpClient ac = new SimpleAjpClient();
      ac.connect(url.getHost(), url.getPort());

      TesterAjpMessage req = ac.createForwardMessage(url.getPath());
      String[] headers = cl.getOptionValues("H");
      for (String h : headers) {
        int c = h.indexOf(":");
        req.addHeader(h.substring(0, c), h.substring(c + 1));
      }
      req.setQuery(url.getQuery());
      req.end();

      TesterAjpMessage resp = ac.sendMessage(req);
      System.out.println(resp.readString());

      ac.disconnect();
    } catch (UnrecognizedOptionException uoe) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("ajp-client [-H <arg>] <Url>", options);
    }catch(Exception e) {
      e.printStackTrace();
    }
  }
}

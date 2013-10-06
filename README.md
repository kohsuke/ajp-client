AJP 1.3 Client
==============

This small library is a Java AJP client code extracted from Apache Tomcat test harness. This is
not yet a general-purpose client connector; it takes more work to turn it into one.

First, look at [AJP Protocol Reference](http://tomcat.apache.org/connectors-doc/ajp/ajpv13a.html)
to understand how the protocol works.

The following code examples shows a usage of this library in the original Tomcat test code:

    // open connection
    SimpleAjpClient ac = new SimpleAjpClient();
    ac.connect("localhost",10059);

    // create a message that indicates the beginning of the request
    TesterAjpMessage forwardMessage = ac.createForwardMessage("/test.xxx");
    forwardMessage.addHeader("content-length","0");
    forwardMessage.end();


    for (int i = 0; i < 2; i++) {
        TesterAjpMessage responseHeaders = ac.sendMessage(forwardMessage);
        // Expect 3 packets: headers, body, end
        validateResponseHeaders(responseHeaders, 200);
        TesterAjpMessage responseBody = ac.readMessage();
        validateResponseBody(responseBody, "Hello");
        validateResponseEnd(ac.readMessage(), true);

        // Give connections plenty of time to time out
        Thread.sleep(2000);

        // Double check the connection is still open
        validateCpong(ac.cping());
    }

    ac.disconnect();

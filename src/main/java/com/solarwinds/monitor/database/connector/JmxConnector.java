package com.solarwinds;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * Hello world!
 *
 */
public class JMXConnector
{
    public static void main(String[] args) {
        // JMX connection parameters
        String jmxUrl = "service:jmx:rmi:///jndi/rmi://localhost:7199/jmxrmi";
        String username = null;
        String password = null;

        // Keyspace name
        String keyspaceName = "mydb";

        try {
            // Connect to JMX
            JMXServiceURL jmxServiceUrl = new JMXServiceURL(jmxUrl);
            Map<String, Object> env = new HashMap<>();
            /*if (username != null && password != null) {
                env.put(JMXConnector.CREDENTIALS, new String[]{username, password});
            }*/
            JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceUrl, env);
            MBeanServerConnection mbeanConnection = jmxConnector.getMBeanServerConnection();

            // Retrieve keyspace metrics
            //String objectNameStr = "org.apache.cassandra.db:type=StorageService";
            String objectNameStr = "org.apache.cassandra.metrics:type=Table,keyspace=mydb,scope=books,name=ReadLatency";
            ObjectName objectName = new ObjectName(objectNameStr);
            System.out.println("Read Latency count " +  mbeanConnection.getAttribute(objectName, "Count"));
            System.out.println("Read Latency One Minute Rate " +  mbeanConnection.getAttribute(objectName, "OneMinuteRate"));
            System.out.println("Read Latency Five Minute Rate " +  mbeanConnection.getAttribute(objectName, "FiveMinuteRate"));
            System.out.println("Read Latency Fifteen Minute Rate " +  mbeanConnection.getAttribute(objectName, "FifteenMinuteRate"));
            System.out.println("Read Total Latency " + mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.metrics:type=Keyspace,keyspace=mydb,name=ReadTotalLatency"), "Count"));

            System.out.println("Write Latency count " +  mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.metrics:type=Keyspace,keyspace=mydb,name=WriteLatency"), "Count"));
            System.out.println("Write Latency One Minute Rate " +  mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.metrics:type=Keyspace,keyspace=mydb,name=WriteLatency"), "OneMinuteRate"));
            System.out.println("Write Latency Five Minute Rate " +  mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.metrics:type=Keyspace,keyspace=mydb,name=WriteLatency"), "FiveMinuteRate"));
            System.out.println("Write Latency Fifteen Minute Rate " +  mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.metrics:type=Keyspace,keyspace=mydb,name=WriteLatency"), "FifteenMinuteRate"));
            System.out.println("Write Total Latency " + mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.metrics:type=Keyspace,keyspace=mydb,name=WriteTotalLatency"), "Count"));


            System.out.println("Total Disk Space Used " + mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.metrics:type=Keyspace,keyspace=mydb,name=TotalDiskSpaceUsed"), "Value"));

            System.out.println("Live Nodes: " +  mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.db:type=StorageService"), "LiveNodes"));
            System.out.println("Unresponsive Nodes: " + mbeanConnection.getAttribute(new ObjectName("org.apache.cassandra.db:type=StorageService"), "UnreachableNodes"));

            // Close the JMX connection
            jmxConnector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

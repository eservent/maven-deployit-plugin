package com.xebialabs.experiments.mojo_victim;

import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.xebialabs.deployit.ci.Host;
import com.xebialabs.deployit.ci.HostAccessMethod;
import com.xebialabs.deployit.ci.OperatingSystemFamily;
import com.xebialabs.deployit.plugin.jbossas.ci.JbossasQueue;
import com.xebialabs.deployit.plugin.jbossas.ci.JbossasServer;
import com.xebialabs.deployit.plugin.jbossas.ci.JbossasVersion;
import com.xebialabs.deployit.plugin.was.ci.WasCell;
import com.xebialabs.deployit.plugin.was.ci.WasCluster;
import com.xebialabs.deployit.plugin.was.ci.WasNode;

public class App {

	public static void main(String[] args) {
		Host host = new Host();
		host.setOperatingSystemFamily(OperatingSystemFamily.UNIX);
		host.setAccessMethod(HostAccessMethod.SSH_SFTP);
		host.setUsername("root");
		host.setPassword("centos");

		JbossasServer jbossasServer = new JbossasServer();
		jbossasServer.setHome("/opt/jboss/4.2");
		jbossasServer.setVersion(JbossasVersion.JBOSSAS_40);
		jbossasServer.setHost(host);

		Host wasHost = new Host();
		wasHost.setAddress("was-61");
		wasHost.setOperatingSystemFamily(OperatingSystemFamily.UNIX);
		wasHost.setAccessMethod(HostAccessMethod.SSH_SCP);
		wasHost.setUsername("was");
		wasHost.setPassword("was");

		WasCell cell = new WasCell();
		cell.setName("was-61-cell");
		cell.setWasHome("/opt/ws/6.1/profiles/dmgr");
		cell.setUsername("wsadmin");
		cell.setPassword("wasdmin");
		cell.setHost(wasHost);

		Set<WasNode> nodes = new HashSet<WasNode>();
		nodes.add(new WasNode());
		WasCluster wasCluster = new WasCluster();
		wasCluster.setCell(cell);
		wasCluster.setName("existing-cluster");
		wasCluster.setNodes(nodes);

		JbossasQueue q = new JbossasQueue();
		q.setName("maven-generated-queue");
		q.setMaxDepth(768);
		q.setJndiName("jms/maven-generated-q");

		XStream xs = new XStream();
		xs.alias(JbossasServer.class.getSimpleName(), JbossasServer.class);
		xs.alias(WasCluster.class.getSimpleName(), WasCluster.class);
		String xml = xs.toXML(jbossasServer);
		System.out.println(xml);

		String xmlWas = xs.toXML(wasCluster);
		System.out.println(xmlWas);

		String qXml = xs.toXML(q);
		System.out.println(qXml);
	}
}

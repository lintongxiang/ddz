package com.ddz.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class Config {
	private static Element root = null;
	static Document xmldoc;
	static {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			factory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder db = factory.newDocumentBuilder();
			xmldoc = db.parse(new File("src/com/ddz/config/config.xml"));
			root = xmldoc.getDocumentElement();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取默认用户名
	public static String getUsername() {
		return ((Element) selectSingleNode("/Config/default-user/username",
				root)).getTextContent();
	}

	// 获取默认密码
	public static String getPassword() {
		return ((Element) selectSingleNode("/Config/default-user/password",
				root)).getTextContent();
	}

	// 修改默认用户
	public static void excuteDefaultUser(String username,String password){
		((Element) selectSingleNode("/Config/default-user/username",
				root)).setTextContent(username);
		((Element) selectSingleNode("/Config/default-user/password",
				root)).setTextContent(password);
		saveXml(xmldoc);
	}
	// 修改客户端配置
	public static void excuteClientConfig(String host, int port) {
		Element hostElement = (Element) selectSingleNode(
				"/Config/ClientConfig/host", root);
		Element portElement = (Element) selectSingleNode(
				"/Config/ClientConfig/port", root);
		hostElement.setTextContent(host);
		portElement.setTextContent(port + "");
		saveXml(xmldoc);
	}

	// 得到客户端端口
	public static int getClientPort() {
		return Integer.parseInt(((Element) selectSingleNode(
				"/Config/ClientConfig/port", root)).getTextContent());
	}

	public static int getServerPort() {
		return Integer.parseInt(((Element) selectSingleNode(
				"/Config/ServerConfig/port", root)).getTextContent());
	}

	public static String getClientHost() {
		return ((Element) selectSingleNode("/Config/ClientConfig/host", root))
				.getTextContent();
	}

	public static String getServerHost() {
		return ((Element) selectSingleNode("/Config/ServerConfig/host", root))
				.getTextContent();
	}

	// 查找节点，并返回第一个符合条件节点
	private static Node selectSingleNode(String express, Object source) {
		Node result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (Node) xpath
					.evaluate(express, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return result;
	}

	// 保存文件
	private static void saveXml(Document document) {

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new FileOutputStream(
					"src/com/ddz/config/config.xml"));

			transformer.transform(domSource, streamResult);

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}




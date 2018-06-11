package test.java.com.redhat.xml.ls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.redhat.xml.ls.parser.XMLNodes.XMLNode;
import com.redhat.xml.ls.parser.XMLNodes.XMLDocumentNode;
import com.redhat.xml.ls.parser.XMLNodes.XMLElementNode;
import com.redhat.xml.ls.parser.XMLParser;
import com.redhat.xml.ls.parser.XMLNodes.XMLAttributeNode;

import org.junit.jupiter.api.Test;

/**
 */
public class ParserTests {

  private XMLNode runParser(String uri, String content) {
    XMLParser parser = new XMLParser(null);
    parser.parse(uri, content);
    return parser.getRoot();
  }

  @Test
  public void testDocument() {
    XMLDocumentNode node = (XMLDocumentNode) runParser("test", "<project><atag/></project>");
    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertNotNull(node.start);
    assertNotNull(node.end);
    assertEquals(1, node.start.line);
    assertEquals(1, node.end.line);
    assertEquals(1, node.start.column);
    assertEquals(27, node.end.column);
  }

  @Test
  public void testSingleElementTag() {
    XMLDocumentNode node = (XMLDocumentNode) runParser("test", "<project />");
    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertEquals("project", node.children[0].name);
    assertNotNull(node.start);
    assertNotNull(node.end);
    assertEquals(1, node.start.line);
    assertEquals(1, node.end.line);
    assertEquals(1, node.start.column);
    assertEquals(12, node.end.column);
  }

  @Test
  public void testAttributeRecognized() {
    String content = "<project attribute=\"hello world\"></project>";
    XMLNode node = runParser("test", content);

    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertEquals(1, node.children[0].children.length);
    assertNotNull(node.start);
    assertNotNull(node.end);
    assertEquals(1, node.start.line);
    assertEquals(1, node.end.line);
    assertEquals(1, node.start.column);
    assertEquals(44, node.end.column);
  }

  @Test
  public void testMultipleAttributesRecognized() {
    XMLNode node = runParser("test", "<project a1=\"world\" a2=\"world\" a3=\"!\"></project>");
    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertEquals(3, node.children[0].children.length);
    assertNotNull(node.start);
    assertNotNull(node.end);
    assertEquals(1, node.start.line);
    assertEquals(1, node.end.line);
    assertEquals(1, node.start.column);

  }

  @Test
  public void testAttributeProperties() {
    XMLNode node = runParser("test", "<project a1=\"world\"></project>");
    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertEquals(1, node.children[0].children.length);
    assertEquals("a1", node.children[0].children[0].name);
    assertEquals("project", node.children[0].children[0].parent.name);
    assertEquals("world", node.children[0].children[0].value);
    assertEquals(10, ((XMLAttributeNode) node.children[0].children[0]).start.column);
    assertEquals(19, ((XMLAttributeNode) node.children[0].children[0]).end.column);
    assertNotNull(node.start);
    assertNotNull(node.end);
    assertNotNull(node.children[0].children[0].parent);
    assertNull(node.children[0].children[0].children);
  }

  @Test
  public void testAttributePositionSpaces() {
    XMLNode node = runParser("test", "<project a1   =   \"world\"></project>");
    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertEquals(1, node.children[0].children.length);
    assertEquals("a1", node.children[0].children[0].name);
    assertEquals("project", node.children[0].children[0].parent.name);
    assertEquals("world", node.children[0].children[0].value);
    assertEquals(10, ((XMLAttributeNode) node.children[0].children[0]).start.column);
    assertEquals(25, ((XMLAttributeNode) node.children[0].children[0]).end.column);
    assertNotNull(node.start);
    assertNotNull(node.end);
    assertNotNull(node.children[0].children[0].parent);
    assertNull(node.children[0].children[0].children);
  }

  @Test
  public void testElement() {
   /* @formatter:off*/
   XMLNode node = runParser("test", "<project> \n" +
    "  someText \n" +
    "</project> \n");
   /* @formatter:on */
    assertNotNull(node);
    node = node.children[0];
    assertNotNull(node);
    assertEquals(XMLNode.ELEMENT_NODE, node.nodeType);
    assertEquals("project", node.name);
    assertEquals(1, node.start.line);
    assertEquals(3, node.end.line);
    assertEquals(11, node.end.column);
  }

  @Test
  public void testXmlDecl() {
   /* @formatter:off */
   XMLNode node = runParser("test", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
   "<project> \n" +
    "  someText \n" +
    "</project> \n");
   /* @formatter:on */
    assertNotNull(node);
    node = node.children[0];
    assertNotNull(node);
    assertEquals(XMLNode.XML_DECL, node.nodeType);

  }

  @Test
  public void testElementText() {
   /* @formatter:off */
   XMLNode node = runParser("test", "<project att=\"k\">Hey Dude</project>");
   /* @formatter:on */
    assertEquals(18, 12);
  }

  /**
   * 
   */
  @Test
  public void testMultipleAttributes() {
    XMLNode node = runParser("test", "<project att=\"k\" att2=\"yo\" att3=\"hi\">Hey Dude</project>");
    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertEquals(3, node.children[0].children.length);

    assertEquals("att", node.children[0].children[0].name);
    assertEquals("k", node.children[0].children[0].value);

    assertEquals("att2", node.children[0].children[1].name);
    assertEquals("yo", node.children[0].children[1].value);

    assertEquals("att3", node.children[0].children[2].name);
    assertEquals("hi", node.children[0].children[2].value);

  }

  /**
   * 
   */
  @Test
  public void testMultipleAttributesPositions() {
    XMLNode node = runParser("test", "<project att=\"k\" att2=\"yo\" att3=\"hi\">Hey Dude</project>");
    assertNotNull(node);
    assertEquals(XMLNode.DOCUMENT_NODE, node.nodeType);
    assertNotNull(node.children);
    assertEquals(1, node.children.length);
    assertEquals(3, node.children[0].children.length);

    assertEquals(10, node.children[0].children[0].start.column);
    assertEquals(16, node.children[0].children[0].end.column);

    assertEquals(18, node.children[0].children[1].start.column);
    assertEquals(26, node.children[0].children[1].end.column);

    assertEquals(28, node.children[0].children[2].start.column);
    assertEquals(36, node.children[0].children[2].end.column);

  }
}
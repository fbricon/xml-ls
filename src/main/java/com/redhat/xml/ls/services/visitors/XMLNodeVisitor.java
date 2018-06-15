package com.redhat.xml.ls.services.visitors;

import java.util.List;

import com.redhat.xml.ls.parser.XMLNodes.XMLAttributeNode;
import com.redhat.xml.ls.parser.XMLNodes.XMLContentNode;
import com.redhat.xml.ls.parser.XMLNodes.XMLDeclarationNode;
import com.redhat.xml.ls.parser.XMLNodes.XMLDocumentNode;
import com.redhat.xml.ls.parser.XMLNodes.XMLElementNode;
import com.redhat.xml.ls.parser.XMLNodes.XMLNode;

public interface XMLNodeVisitor {
    public void visit(XMLNode xmlNode);

    public void visit(XMLAttributeNode attrNode);

    public void visit(XMLContentNode contentNode);

    public void visit(XMLDeclarationNode declNode);

    public void visit(XMLDocumentNode documentNode);

    public void visit(XMLElementNode documentNode);

    public List<?> getList();
}
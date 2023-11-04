#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <libxml/HTMLparser.h>
#include <iostream>
#include <string>

void traverse_dom_trees(xmlNode * a_node, std::string &node_name)
{
    xmlNode *cur_node = NULL;
	//std::cout << "New iteration: " << std::endl;

    if(NULL == a_node)
    {
        //printf("Invalid argument a_node %p\n", a_node);
        return;
    }

    for (cur_node = a_node; cur_node; cur_node = cur_node->next) 
    {
        if (cur_node->type == XML_ELEMENT_NODE) 
        {
			node_name = (const char *) cur_node->name;
            /* Check for if current node should be exclude or not */
            //printf("Node type: Text, name: %s\n", cur_node->name);
        }
        else if(cur_node->type == XML_TEXT_NODE)
        {
			if (node_name == "p" || node_name == "strong") {
            	/* Process here text node, It is available in cpStr :TODO: */
				std::cout << "Node name: " << node_name << std::endl;
            	printf("node type: Text, node content: %s,  content length %lu\n", (char *)cur_node->content, strlen((char *)cur_node->content));
			}
        }
        traverse_dom_trees(cur_node->children, node_name);
    }
}

static void printTitle(xmlDoc *doc, xmlNode * a_node)
{
    xmlNode *cur_node = NULL;

    for (cur_node = a_node; cur_node; cur_node = cur_node->next) {
        if (cur_node->type == XML_ELEMENT_NODE && 
			(
				xmlStrcmp(cur_node->name, (const xmlChar *)"p") == 0 || (
					strlen((char *) cur_node->name) == 2 &&
					(char) cur_node->name[0] == 'h' &&
					isdigit((char) cur_node->name[1])
				)
			)

		) {
            xmlChar* content;
            content = xmlNodeListGetString(doc, cur_node->xmlChildrenNode, 1);
            printf("*********************** node type: Element, name: %s, content: %s\n", cur_node->name, content);
            xmlFree(content);
        }

        printTitle(doc, cur_node->children);
    }
}

static void searchMeta(xmlDoc *doc, xmlNode * a_node)
{
    xmlNode *cur_node = NULL;

    for (cur_node = a_node; cur_node; cur_node = cur_node->next) {
        if (cur_node->type == XML_ELEMENT_NODE && xmlStrcmp(cur_node->name, (const xmlChar *)"meta") == 0) {
			xmlAttr* attribute = cur_node->properties;
			bool searchForContent{ false };
			while(attribute) {
				auto name = (char *) attribute->name;
				auto value = reinterpret_cast<char *>(xmlNodeListGetString(doc, attribute->children, 1));
				if (searchForContent) {
					if(strcmp(name, "content") == 0) {
						std::cout << value << std::endl;
						searchForContent = false;
					}
				}
				if (strncmp(value, "og", 2) == 0) {
					std::cout << name << ": " << value << "; content: ";
					searchForContent = true;
				}
				//do something with value
				xmlFree(value); 
				attribute = attribute->next;
			}
        }

        searchMeta(doc, cur_node->children);
    }
}

int main(int argc, char **argv) 
{
    htmlDocPtr doc;
    xmlNode *roo_element = NULL;

    if (argc != 2)  
    {
        printf("\nInvalid argument\n");
        return(1);
    }

    /* Macro to check API for match with the DLL we are using */
    LIBXML_TEST_VERSION    

    doc = htmlReadFile(argv[1], NULL, HTML_PARSE_NOBLANKS | HTML_PARSE_NOERROR | HTML_PARSE_NOWARNING | HTML_PARSE_NONET);
    if (doc == NULL) 
    {
        fprintf(stderr, "Document not parsed successfully.\n");
        return 0;
    }

    roo_element = xmlDocGetRootElement(doc);

    if (roo_element == NULL) 
    {
        fprintf(stderr, "empty document\n");
        xmlFreeDoc(doc);
        return 0;
    }

    printf("Root Node is %s\n", roo_element->name);
	std::string node_name;
    //traverse_dom_trees(roo_element, node_name);

	//printTitle(doc, roo_element);
	searchMeta(doc, roo_element);

    xmlFreeDoc(doc);       // free document
    xmlCleanupParser();    // Free globals
    return 0;
}


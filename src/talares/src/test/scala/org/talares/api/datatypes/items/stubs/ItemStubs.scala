package org.talares.api.datatypes.items.stubs

import play.api.libs.json.Json

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
object ItemStubs {

  val binaryStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)",
      |    "type": "Tridion.ContentDelivery.Binary"
      |  },
      |  "BinaryId": 123,
      |  "PublicationId": 1,
      |  "Type": "application/vnd.ms-fontobject",
      |  "BinaryVariants": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)/BinaryVariants"
      |    }
      |  }
      |}""".stripMargin
  )

  val binariesStub = Json.arr(binaryStub, binaryStub, binaryStub)

  val binaryContentStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)",
      |    "type": "Tridion.ContentDelivery.BinaryContent"
      |  },
      |  "BinaryId": 123,
      |  "PublicationId": 1,
      |  "VariantId": "123"
      |}""".stripMargin
  )

  val binaryContentsStub = Json.arr(binaryContentStub, binaryContentStub, binaryContentStub)

  val binaryVariantStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/BinaryVariants(BinaryId=123,PublicationId=1,VariantId='default')",
      |    "type": "Tridion.ContentDelivery.BinaryVariant"
      |  },
      |  "URLPath": "/dir/image.jpg",
      |  "BinaryId": 123,
      |  "Description": null,
      |  "IsComponent": true,
      |  "Path": "/dir/image.jpg",
      |  "PublicationId": 1,
      |  "Type": "image/jpeg",
      |  "VariantId": "default",
      |  "Binary": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/BinaryVariants(BinaryId=123,PublicationId=1,VariantId='default')/Binary"
      |    }
      |  }
      |}""".stripMargin
  )

  val binaryVariantsStub = Json.arr(binaryVariantStub, binaryVariantStub, binaryVariantStub)

  val componentStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)",
      |    "type": "Tridion.ContentDelivery.Component"
      |  },
      |  "Author": "User",
      |  "CreationDate": "/Date(1362655474000+60)/",
      |  "InitialPublishDate": "/Date(1374139562000+120)/",
      |  "Multimedia": false,
      |  "ItemId": 123,
      |  "LastPublishDate": "/Date(1374139562113+120)/",
      |  "MajorVersion": 1,
      |  "MinorVersion": 2,
      |  "ModificationDate": "/Date(1374139455000+120)/",
      |  "OwningPublication": 1,
      |  "PublicationId": 1,
      |  "SchemaId": 123,
      |  "Title": "require.js",
      |  "Schema": {
      |    "__deferred": {
      |        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Schema"
      |    }
      |  },
      |  "ComponentPresentations": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/ComponentPresentations"
      |    }
      |  },
      |  "Keywords": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Keywords"
      |    }
      |  },
      |  "CustomMetas": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/CustomMetas"
      |    }
      |  }
      |}""".stripMargin
  )

  val componentsStub = Json.arr(componentStub, componentStub, componentStub)

  val componentPresentationStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)",
      |    "type": "Tridion.ContentDelivery.ComponentPresentation"
      |  },
      |  "ComponentId": 123,
      |  "OutputFormat": "HTML Fragment",
      |  "PresentationContent": "<h1>Test</h1>",
      |  "PublicationId": 1,
      |  "TemplateId": 123,
      |  "Component": {
      |    "__deferred": {
      |        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Component"
      |    }
      |  },
      |  "Template": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Template"
      |    }
      |  },
      |  "Pages": {
      |    "__deferred": {
      |       "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Pages"
      |    }
      |  }
      |}""".stripMargin
  )

  val componentPresentationsStub = Json.arr(componentPresentationStub, componentPresentationStub, componentPresentationStub)

  val customMetaStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)",
      |    "type": "Tridion.ContentDelivery.CustomMeta"
      |  },
      |  "DateValue": null,
      |  "FloatValue": null,
      |  "Id": 123,
      |  "ItemId": 123,
      |  "ItemType": 1,
      |  "KeyName": "testKey",
      |  "PublicationId": 1,
      |  "StringValue": "Test",
      |  "Component": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Component"
      |    }
      |  },
      |  "Page": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Page"
      |    }
      |  },
      |  "Keyword": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Keyword"
      |    }
      |  }
      |}""".stripMargin
  )

  val customMetasStub = Json.arr(customMetaStub, customMetaStub, customMetaStub)

  val keywordStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(PublicationId=1,Id=2)",
      |    "type": "Tridion.ContentDelivery.Keyword"
      |  },
      |  "Id": 123,
      |  "PublicationId": 1,
      |  "TaxonomyId": 123,
      |  "Title": "test",
      |  "Description": "test",
      |  "HasChildren": false,
      |  "Abstract": false,
      |  "Navigable": false,
      |  "Key": "test",
      |  "Depth": 0,
      |  "ItemType": 1,
      |  "TotalRelatedItems": 1,
      |  "Components": {
      |    "__deferred": {
      |        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Components"
      |    }
      |  },
      |  "Pages": {
      |    "__deferred": {
      |        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Pages"
      |    }
      |  },
      |  "CustomMetas": {
      |    "__deferred": {
      |        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/CustomMetas"
      |    }
      |  },
      |  "Children": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Children"
      |    }
      |  },
      |  "Parent": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Parent"
      |    }
      |  }
      |}""".stripMargin
  )

  val keywordsStub = Json.arr(keywordStub, keywordStub, keywordStub)

  val metadataStub = Json.parse(
    """{
      |  "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc",
      |  "type": "Tridion.ContentDelivery.SomeContentType"
      |}""".stripMargin
  )

  val pageStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)",
      |    "type": "Tridion.ContentDelivery.Page"
      |  },
      |  "Author": null,
      |  "CreationDate": "/Date(1362655241000+60)/",
      |  "InitialPublishDate": "/Date(1371220702600+120)/",
      |  "ItemId": 123,
      |  "LastPublishDate": "/Date(1371793702520+120)/",
      |  "MajorVersion": 1,
      |  "MinorVersion": 2,
      |  "ModificationDate": "/Date(1363341350000+60)/",
      |  "OwningPublication": 1,
      |  "PagePath": "/path/file.doc",
      |  "PublicationId": 1,
      |  "TemplateId": 123,
      |  "Title": "A File",
      |  "Url": "/path/file.doc",
      |  "PageContent": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/PageContent"
      |    }
      |  },
      |  "StructureGroup": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/StructureGroup"
      |    }
      |  },
      |  "ComponentPresentations": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/ComponentPresentations"
      |    }
      |  },
      |  "Keywords": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/Keywords"
      |    }
      |  },
      |  "CustomMetas": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/CustomMetas"
      |    }
      |  }
      |}""".stripMargin
  )

  val pagesStub = Json.arr(pageStub, pageStub, pageStub)

  val pageContentStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/PageContents(PageId=123,PublicationId=1)",
      |    "type": "Tridion.ContentDelivery.PageContent"
      |  },
      |  "CharSet": "UTF8",
      |  "Content": "<h1>Test</h1>",
      |  "PageId": 123,
      |  "PublicationId": 1,
      |  "Page": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/PageContents(PageId=123,PublicationId=1)/Page"
      |    }
      |  }
      |}""".stripMargin
  )

  val pageContentsStub = Json.arr(pageContentStub, pageContentStub, pageContentStub)

  val publicationStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)",
      |    "type": "Tridion.ContentDelivery.Publication"
      |  },
      |  "Id": 1,
      |  "Key": "Test Key",
      |  "MultimediaPath": "\\Multimedia",
      |  "MultimediaUrl": "/Multimedia",
      |  "PublicationPath": "\\",
      |  "PublicationUrl": "/",
      |  "Title": "Test Title",
      |  "Schemas": {
      |    "__deferred": {
      |        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Schemas"
      |    }
      |  },
      |  "ComponentPresentations": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/ComponentPresentations"
      |    }
      |  },
      |  "Keywords": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Keywords"
      |    }
      |  },
      |  "Binaries": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Binaries"
      |    }
      |  },
      |  "BinaryVariants": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/BinaryVariants"
      |    }
      |  },
      |  "Components": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Components"
      |    }
      |  },
      |  "CustomMetas": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/CustomMetas"
      |    }
      |  },
      |  "Pages": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Pages"
      |    }
      |  },
      |  "PageContents": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/PageContents"
      |    }
      |  },
      |  "StructureGroups": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/StructureGroups"
      |    }
      |  },
      |  "Templates": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Templates"
      |    }
      |  }
      |}""".stripMargin
  )

  val publicationsStub = Json.arr(publicationStub, publicationStub, publicationStub)

  val schemaStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Schemas(SchemaId=123,PublicationId=1)",
      |    "type": "Tridion.ContentDelivery.Schema"
      |  },
      |  "PublicationId": 1,
      |  "SchemaId": 123,
      |  "Title": "Test title",
      |  "Components": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Schemas(SchemaId=123,PublicationId=1)/Components"
      |    }
      |  }
      |}""".stripMargin
  )

  val schemasStub = Json.arr(schemaStub, schemaStub, schemaStub)

  val structureGroupStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(PublicationId=1,Id=2)",
      |    "type": "Tridion.ContentDelivery.StructureGroup"
      |  },
      |  "Depth": 0,
      |  "Directory": null,
      |  "Id": 123,
      |  "PublicationId": 1,
      |  "Title": "Root",
      |  "Pages": {
      |    "__deferred": {
      |        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Pages"
      |    }
      |  },
      |  "Children": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Children"
      |    }
      |  },
      |  "Parent": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Parent"
      |    }
      |  }
      |}""".stripMargin
  )

  val structureGroupsStub = Json.arr(structureGroupStub, structureGroupStub, structureGroupStub)

  val templateStub = Json.parse(
    """{
      |  "__metadata": {
      |    "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)",
      |    "type": "Tridion.ContentDelivery.Template"
      |  },
      |  "Author": "User",
      |  "CreationDate": "/Date(1399546761343+120)/",
      |  "InitialPublishDate": "/Date(1399546761343+120)/",
      |  "ItemId": 123,
      |  "LastPublishDate": "/Date(1399546761343+120)/",
      |  "MajorVersion": 1,
      |  "MinorVersion": 2,
      |  "ModificationDate": "/Date(1399546761343+120)/",
      |  "OutputFormat": "HTML Fragment",
      |  "OwningPublication": 0,
      |  "PublicationId": 1,
      |  "TemplatePriority": 200,
      |  "Title": "Test Item",
      |  "ComponentPresentations": {
      |    "__deferred": {
      |      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)/ComponentPresentations"
      |    }
      |  }
      |}""".stripMargin
  )

  val templatesStub = Json.arr(templateStub, templateStub, templateStub)
}

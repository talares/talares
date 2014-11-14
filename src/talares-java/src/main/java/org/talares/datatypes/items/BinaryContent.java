package org.talares.datatypes.items;

/**
 * A Java representation of a BinaryContent content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.BinaryContent"
 *    },
 *    "BinaryId": 123,
 *    "PublicationId": 1,
 *    "VariantId": "123"
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class BinaryContent extends Item {

  private final int binaryId;
  private final String variantId;

  public BinaryContent(final org.talares.api.Talares api,
                       final org.talares.api.datatypes.items.BinaryContent scalaBinaryContent) {
    super(api, scalaBinaryContent);
    this.binaryId = scalaBinaryContent.binaryId();
    this.variantId = scalaBinaryContent.variantId();
  }

  public final int getBinaryId() {
    return binaryId;
  }

  public final String getVariantId() {
    return variantId;
  }
}
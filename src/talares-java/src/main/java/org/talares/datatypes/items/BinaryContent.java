package org.talares.datatypes.items;

/**
 * A Java representation of a BinaryContent content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    &quot;__metadata&quot;: {
 *      &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)&quot;,
 *      &quot;type&quot;: &quot;Tridion.ContentDelivery.BinaryContent&quot;
 *    },
 *    &quot;BinaryId&quot;: 123,
 *    &quot;PublicationId&quot;: 1,
 *    &quot;VariantId&quot;: &quot;123&quot;
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
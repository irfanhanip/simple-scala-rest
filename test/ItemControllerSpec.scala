
import play.api.test.FakeRequest
import play.api.test.PlaySpecification
import play.api.test.WithApplication

/**
 * Created by hanip on 4/30/16.
 */
class ItemControllerSpec extends PlaySpecification {
  "render the items page" in new WithApplication {
    val home = route(FakeRequest(GET, "/items")).get

    status(home) mustEqual OK
    contentType(home) must beSome.which(_ == "application/json")
  }
}

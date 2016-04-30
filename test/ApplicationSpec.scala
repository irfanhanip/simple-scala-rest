package test

import play.api.test.FakeRequest
import play.api.test.PlaySpecification
import play.api.test.WithApplication

class ApplicationSpec extends PlaySpecification {

    "Application" should {
        "send 404 on a bad request" in new WithApplication {
            val result = route(FakeRequest(GET, "/boum")).get
            status(result) mustEqual NOT_FOUND
        }
    }
}

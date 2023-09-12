/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unit.domain

import base.UnitSpec
import uk.gov.hmrc.eoricommoncomponent.frontend.domain.messaging.subscription.CustomsDataStoreRequest

@this(layout_di: layout)
@(service: Service)(implicit request: Request[_], messages: Messages)

  private val customsDataStoreRequest =
    CustomsDataStoreRequest(eori = "123456789000", address = "address", timestamp = "timestamp")

@layout_di(messages("cds.not-based-in-uk.page.title"), service = service) {
    <div>
        <h1 class="govuk-heading-l">@messages("cds.not-based-in-uk.heading")</h1>

    "convert to map" in {
      val asMap = customsDataStoreRequest.toMap(this)
      asMap shouldBe Map("eori" -> "123456789000", "address" -> "address", "timestamp" -> "timestamp")

    }
  }

}

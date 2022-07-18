/*
 * Copyright 2022 HM Revenue & Customs
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

package uk.gov.hmrc.eoricommoncomponent.frontend.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import uk.gov.hmrc.eoricommoncomponent.frontend.controllers.auth.AuthAction
import uk.gov.hmrc.eoricommoncomponent.frontend.controllers.routes._
import uk.gov.hmrc.eoricommoncomponent.frontend.domain.subscription.ContactDetailsSubscriptionFlowPageGetEori
import uk.gov.hmrc.eoricommoncomponent.frontend.domain.LoggedInUserWithEnrolments
import uk.gov.hmrc.eoricommoncomponent.frontend.forms.ContactDetailsForm.contactDetailsCreateForm
import uk.gov.hmrc.eoricommoncomponent.frontend.forms.models.{AddressViewModel, ContactDetailsViewModel}
import uk.gov.hmrc.eoricommoncomponent.frontend.models.Service
import uk.gov.hmrc.eoricommoncomponent.frontend.services.{SubscriptionBusinessService, SubscriptionDetailsService}
import uk.gov.hmrc.eoricommoncomponent.frontend.services.cache.SessionCache
import uk.gov.hmrc.eoricommoncomponent.frontend.services.countries.Countries
import uk.gov.hmrc.eoricommoncomponent.frontend.services.SubscriptionDetailsService
import uk.gov.hmrc.eoricommoncomponent.frontend.views.html.contact_address
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactAddressController @Inject() (
  authAction: AuthAction,
  subscriptionBusinessService: SubscriptionBusinessService,
  cdsFrontendDataCache: SessionCache,
  subscriptionFlowManager: SubscriptionFlowManager,
  subscriptionDetailsService: SubscriptionDetailsService,
  mcc: MessagesControllerComponents,
  contactAddressView: contact_address
)(implicit ec: ExecutionContext)
    extends CdsController(mcc) {

  def createForm(service: Service): Action[AnyContent] =
    authAction.ggAuthorisedUserWithEnrolmentsAction { implicit request => _: LoggedInUserWithEnrolments =>
      populateFormGYE(service)(false)
    }

  def reviewForm(service: Service): Action[AnyContent] =
    authAction.ggAuthorisedUserWithEnrolmentsAction { implicit request => _: LoggedInUserWithEnrolments =>
      populateFormGYE(service)(true)
    }

  private def populateFormGYE(
    service: Service
  )(isInReviewMode: Boolean)(implicit request: Request[AnyContent]): Future[Result] =
    subscriptionBusinessService.cachedContactDetailsModel.flatMap { contactDetails =>
      cdsFrontendDataCache.email.flatMap { email =>
        populateOkView(contactDetails.map(_.toContactsInfoViewModel), isInReviewMode = isInReviewMode, service)
      }
    }

  def submit(isInReviewMode: Boolean, service: Service): Action[AnyContent] =
    authAction.ggAuthorisedUserWithEnrolmentsAction { implicit request => _: LoggedInUserWithEnrolments =>
      contactDetailsCreateForm().bindFromRequest.fold(
        formWithErrors =>
          createContactDetails().map { contactDetails =>
            BadRequest(contactAddressView(formWithErrors, Countries.all, contactDetails, isInReviewMode, service))
          },
        formData => storeContactDetails(formData, isInReviewMode, service)
      )
    }

  private def createContactDetails()(implicit request: Request[AnyContent]): Future[AddressViewModel] =
    cdsFrontendDataCache.registrationDetails.map(rd => AddressViewModel(rd.address))

  private def populateOkView(
    contactDetailsModel: Option[ContactDetailsViewModel],
    isInReviewMode: Boolean,
    service: Service
  )(implicit request: Request[AnyContent]): Future[Result] = {
    val form = contactDetailsModel
      .fold(contactDetailsCreateForm())(f => contactDetailsCreateForm().fill(f))

    createContactDetails() map (
      contactDetails => Ok(contactAddressView(form, Countries.all, contactDetails, isInReviewMode, service))
    )
  }

  private def storeContactDetails(formData: ContactDetailsViewModel, inReviewMode: Boolean, service: Service)(implicit
    hc: HeaderCarrier,
    request: Request[AnyContent]
  ): Future[Result] =
    subscriptionDetailsService
      .cacheContactDetails(formData.toContactInfoDetailsModel, isInReviewMode = inReviewMode)
      .map(
        _ =>
          if (inReviewMode) Redirect(DetermineReviewPageController.determineRoute(service))
          else
            Redirect(
              subscriptionFlowManager
                .stepInformation(ContactDetailsSubscriptionFlowPageGetEori)
                .nextPage
                .url(service)
            )
      )

}

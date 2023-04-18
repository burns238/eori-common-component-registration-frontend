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

package uk.gov.hmrc.eoricommoncomponent.frontend.domain

import play.api.libs.json.{JsValue, Json}

case class CdsOrganisationType(id: String)

object CdsOrganisationType {
  implicit val formats                                                          = Json.format[CdsOrganisationType]
  implicit def toJsonFormat(mayBeOrgType: Option[CdsOrganisationType]): JsValue = Json.toJson(mayBeOrgType)

  val CompanyId                       = "company"
  val SoleTraderId                    = "sole-trader"
  val IndividualId                    = "individual"
  val PartnershipId                   = "partnership"
  val LimitedLiabilityPartnershipId   = "limited-liability-partnership"
  val CharityPublicBodyNotForProfitId = "charity-public-body-not-for-profit"

  val EUOrganisationId = "eu-organisation"
  val EUIndividualId   = "eu-individual"

  val ThirdCountryOrganisationId = "third-country-organisation"
  val ThirdCountrySoleTraderId   = "third-country-sole-trader"
  val ThirdCountryIndividualId   = "third-country-individual"

  val IsleOfManOrganisationId = "iom-organisation"
  val IsleOfManSoleTraderId   = "iom-sole-trader"
  val IsleOfManIndividualId   = "iom-individual"

  val Company: CdsOrganisationType                       = CdsOrganisationType(CompanyId)
  val SoleTrader: CdsOrganisationType                    = CdsOrganisationType(SoleTraderId)
  val Individual: CdsOrganisationType                    = CdsOrganisationType(IndividualId)
  val Partnership: CdsOrganisationType                   = CdsOrganisationType(PartnershipId)
  val LimitedLiabilityPartnership: CdsOrganisationType   = CdsOrganisationType(LimitedLiabilityPartnershipId)
  val CharityPublicBodyNotForProfit: CdsOrganisationType = CdsOrganisationType(CharityPublicBodyNotForProfitId)

  val EUOrganisation: CdsOrganisationType = CdsOrganisationType(EUOrganisationId)
  val EUIndividual: CdsOrganisationType   = CdsOrganisationType(EUIndividualId)

  val ThirdCountryOrganisation: CdsOrganisationType = CdsOrganisationType(ThirdCountryOrganisationId)
  val ThirdCountrySoleTrader: CdsOrganisationType   = CdsOrganisationType(ThirdCountrySoleTraderId)
  val ThirdCountryIndividual: CdsOrganisationType   = CdsOrganisationType(ThirdCountryIndividualId)

  val IsleOfManOrganisation: CdsOrganisationType = CdsOrganisationType(IsleOfManOrganisationId)
  val IsleOfManSoleTrader: CdsOrganisationType   = CdsOrganisationType(IsleOfManSoleTraderId)
  val IsleOfManIndividual: CdsOrganisationType   = CdsOrganisationType(IsleOfManIndividualId)

  val validOrganisationTypes: Map[String, CdsOrganisationType] = Map(
    CompanyId                       -> Company,
    SoleTraderId                    -> SoleTrader,
    IndividualId                    -> Individual,
    PartnershipId                   -> Partnership,
    LimitedLiabilityPartnershipId   -> LimitedLiabilityPartnership,
    CharityPublicBodyNotForProfitId -> CharityPublicBodyNotForProfit,
    EUOrganisationId                -> EUOrganisation,
    EUIndividualId                  -> EUIndividual,
    ThirdCountryOrganisationId      -> ThirdCountryOrganisation,
    ThirdCountrySoleTraderId        -> ThirdCountrySoleTrader,
    ThirdCountryIndividualId        -> ThirdCountryIndividual,
    IsleOfManOrganisationId         -> IsleOfManOrganisation,
    IsleOfManSoleTraderId           -> IsleOfManSoleTrader,
    IsleOfManIndividualId           -> IsleOfManIndividual
  )

  val IndividualOrganisations: Seq[CdsOrganisationType] = Seq(
    SoleTrader,
    Individual,
    ThirdCountryIndividual,
    ThirdCountrySoleTrader,
    IsleOfManSoleTrader,
    IsleOfManIndividual
  )

  lazy val individualOrganisationIds: Seq[String] =
    Seq(
      SoleTraderId,
      IndividualId,
      ThirdCountryIndividualId,
      ThirdCountrySoleTraderId,
      IsleOfManSoleTraderId,
      IsleOfManIndividualId
    )

  lazy val rowAndIomIndividualOrganisationIds: Seq[String] =
    Seq(ThirdCountryIndividualId, ThirdCountrySoleTraderId, IsleOfManSoleTraderId, IsleOfManIndividualId)

  lazy val iomOnlySoleAndIndividualIds: Seq[String] = Seq(IsleOfManSoleTraderId, IsleOfManIndividualId)

  def forId(organisationTypeId: String): CdsOrganisationType = validOrganisationTypes(organisationTypeId)

}

package investigation.hub.common.web.test.data.dtos.transaction;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder()
@Setter
@AllArgsConstructor
@ToString()
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Transaction {
    @JsonIgnore
    private String subjectId;
    @JsonIgnore
    private Aml aml;
    @JsonProperty("currencyCodeOrigin")
    private String currencyCodeOrigin;
    @JsonProperty("organisationUnit")
    private String organisationUnit;
    @JsonProperty("channel")
    private Channel channel;
    @JsonProperty("description")
    private String description;
    @JsonProperty("txnType")
    private TxnType txnType;
    @JsonProperty("employeeId")
    private String employeeId;
    @JsonProperty("originationDate")
    private String originationDate;
    @JsonProperty("originator")
    private Actor originator;
    @JsonProperty("transactionDate")
    private String transactionDate;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("txnAmountInBaseCurrency")
    private Double txnAmountInBaseCurrency;
    @JsonProperty("currencyCodeBase")
    private String currencyCodeBase;
    @JsonProperty("tellerId")
    private String tellerId;
    @JsonProperty("txnAmountInOriginCurrency")
    private Integer txnAmountInOriginCurrency;
    @JsonProperty("beneficiary")
    private Actor beneficiary;
    @JsonProperty("txnSrcRefId")
    private String txnSrcRefId;
    @JsonProperty("cardId")
    private String cardId;
    @JsonProperty("txnSrcUniqueId")
    private String txnSrcUniqueId;
    @JsonProperty("localTimestamp")
    private String localTimestamp;
    @JsonProperty("investigation")
    private Investigation investigation;
    @JsonProperty("location")
    private String location;
    @JsonProperty("account")
    private Account account;
    @JsonProperty("txnId")
    private String txnId;
    @JsonProperty("creditDebitCode")
    private String creditDebitCode;
}
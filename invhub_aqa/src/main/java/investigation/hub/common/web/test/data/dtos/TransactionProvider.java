package investigation.hub.common.web.test.data.dtos;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.provider.Provider;
import investigation.hub.common.web.test.data.dtos.transaction.Account;
import investigation.hub.common.web.test.data.dtos.transaction.Actor;
import investigation.hub.common.web.test.data.dtos.transaction.Address;
import investigation.hub.common.web.test.data.dtos.transaction.Aml;
import investigation.hub.common.web.test.data.dtos.transaction.Transaction;
import investigation.hub.common.web.test.data.dtos.transaction.TxnType;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Log4j2
public class TransactionProvider implements Provider<Transaction> {
    private Transaction transaction;

    public TransactionProvider(List<String> uiValues) {
        String[] currency = uiValues.get(3).split(" ");
        String originatorCountryCode = getCountryCode(uiValues.get(8));
        String beneficiaryCountryCode = getCountryCode(uiValues.get(6));


        this.transaction = Transaction.builder()
                .account(getAccount(uiValues.get(9)))
                .txnType(getTxnType(uiValues.get(4)))
                .originator(getOriginator(uiValues.get(7), originatorCountryCode))
                .beneficiary(getBeneficiary(uiValues.get(5), beneficiaryCountryCode))
                .txnSrcRefId(uiValues.get(0))
                .creditDebitCode(uiValues.get(2))
                .originationDate(uiValues.get(1))
                .currencyCodeBase(currency[0])
                .txnAmountInBaseCurrency(Double.parseDouble(currency[1].replaceAll(",", "")))
                .build();
    }

    //TODO: update this constructor after testdata is client driven
    public TransactionProvider(Aml aml, String subjectId, boolean isOpenDetection) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String path =
                "src/main/resources/transactions/%s/%s.json".formatted(isOpenDetection ? "open" : "historical", aml.name())
                        .toLowerCase();
        try {
            this.transaction = objectMapper.readValue(new File(path), Transaction.class);
            this.transaction.setAml(aml);
            this.transaction.setSubjectId(subjectId);
        } catch (IOException e) {
            throw new RuntimeException("Deserializing not possible: " + e.getMessage());
        }

    }

    private TxnType getTxnType(String instrument) {
        return TxnType.builder()
                .instrument(instrument)
                .build();
    }

    private String getCountryCode(String countryCode) {
        return countryCode.substring(countryCode.indexOf("(") + 1, countryCode.indexOf(")")).trim();
    }


    private Actor getOriginator(String name, String countryCode) {
        return Actor.builder()
                .name(name)
                .address(getAddress(countryCode))
                .build();
    }

    private Address getAddress(String countryCode) {
        return Address.builder()
                .countryCode(countryCode)
                .build();
    }


    private Actor getBeneficiary(String name, String countryCode) {
        return Actor.builder()
                .name(name)
                .address(getAddress(countryCode))
                .build();
    }

    private Account getAccount(String accountId) {
        return Account.builder()
                .srcRefId(accountId)
                .build();
    }


    @Override
    public Transaction provide() {
        return transaction;
    }
}

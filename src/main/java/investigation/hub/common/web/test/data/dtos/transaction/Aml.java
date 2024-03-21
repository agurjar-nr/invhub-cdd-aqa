package investigation.hub.common.web.test.data.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Aml {
    AML03("A large cash deposit"),
    AML13("A large cheque deposit"),
    AML16("A high risk transaction"),
    AML17("A terrorist haven transaction"),
    AML18("A financial haven transaction"),
    AML50("Large cash deposits and cash withdrawals"),
    AML51("Large check deposits and wire withdrawals"),
    AML52("Large check deposits and cash withdrawals"),
    AML56("A high risk for Human Trafficking transaction"),
    AML57("A high risk for Wildlife Trafficking transaction"),
    AML99("Very strange detection");//added for test purposes only.

    public final String info;

}
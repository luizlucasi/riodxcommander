package com.sgaraba.library.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sgaraba.library.web.rest.TestUtil;

public class ModemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Modem.class);
        Modem modem1 = new Modem();
        modem1.setId(1L);
        Modem modem2 = new Modem();
        modem2.setId(modem1.getId());
        assertThat(modem1).isEqualTo(modem2);
        modem2.setId(2L);
        assertThat(modem1).isNotEqualTo(modem2);
        modem1.setId(null);
        assertThat(modem1).isNotEqualTo(modem2);
    }
}

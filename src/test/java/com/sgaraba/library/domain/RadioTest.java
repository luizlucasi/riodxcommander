package com.sgaraba.library.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sgaraba.library.web.rest.TestUtil;

public class RadioTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Radio.class);
        Radio radio1 = new Radio();
        radio1.setId(1L);
        Radio radio2 = new Radio();
        radio2.setId(radio1.getId());
        assertThat(radio1).isEqualTo(radio2);
        radio2.setId(2L);
        assertThat(radio1).isNotEqualTo(radio2);
        radio1.setId(null);
        assertThat(radio1).isNotEqualTo(radio2);
    }
}

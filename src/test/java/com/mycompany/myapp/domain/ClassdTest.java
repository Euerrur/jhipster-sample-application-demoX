package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classd.class);
        Classd classd1 = new Classd();
        classd1.setId(1L);
        Classd classd2 = new Classd();
        classd2.setId(classd1.getId());
        assertThat(classd1).isEqualTo(classd2);
        classd2.setId(2L);
        assertThat(classd1).isNotEqualTo(classd2);
        classd1.setId(null);
        assertThat(classd1).isNotEqualTo(classd2);
    }
}

package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChangeHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChangeHistory.class);
        ChangeHistory changeHistory1 = new ChangeHistory();
        changeHistory1.setId(1L);
        ChangeHistory changeHistory2 = new ChangeHistory();
        changeHistory2.setId(changeHistory1.getId());
        assertThat(changeHistory1).isEqualTo(changeHistory2);
        changeHistory2.setId(2L);
        assertThat(changeHistory1).isNotEqualTo(changeHistory2);
        changeHistory1.setId(null);
        assertThat(changeHistory1).isNotEqualTo(changeHistory2);
    }
}

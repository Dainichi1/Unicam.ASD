package it.unicam.cs.asdl2223.es2sol;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BurglarTest {

    @Test
    final void testBurglar() {
        assertThrows(NullPointerException.class,
                () -> new Burglar(null));
        new Burglar(new CombinationLock("AAA"));
    }

    @Test
    final void testFindCombination() {
        CombinationLock cl = new CombinationLock("XHS");
        cl.lock();
        Burglar b = new Burglar(cl);
        String comb = b.findCombination();
        System.out.println(comb);
        assertTrue(comb.equals("XHS"));
        assertTrue(b.getAttempts()>0);
    }

}

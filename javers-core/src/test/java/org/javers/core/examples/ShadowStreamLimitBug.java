package org.javers.core.examples;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.fest.assertions.api.Assertions;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.examples.model.Employee;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.junit.Test;

public class ShadowStreamLimitBug {
    @Test
    public void shouldFindShadowsAndStreamWithLimit() {
        // given
        Javers javers = JaversBuilder.javers().build();
        Employee frodo = new Employee("Frodo");
        frodo.addSubordinate(new Employee("Sam"));

        javers.commit("author", frodo);

        //when
        IntStream.range(1,10).forEach( i -> {
            frodo.setSalary(1_000 * i);
            javers.commit("author", frodo);
        });

        Stream<Shadow<Employee>> shadows = javers.findShadowsAndStream(
                QueryBuilder.byInstanceId("Frodo", Employee.class).limit(2).build());

        Assertions.assertThat( shadows.count() == 2 );
    }
}

package org.javers.core.cases

import org.javers.core.JaversBuilder
import org.javers.core.metamodel.annotation.Id
import org.javers.core.metamodel.annotation.TypeName
import org.javers.repository.jql.QueryBuilder
import spock.lang.Specification

import java.time.LocalDate

class CaseWithCompositeId extends Specification{

    @TypeName("Person")
    class Person {
        @Id String name
        @Id String surname
        @Id LocalDate dob

        String data
    }

    def "should  "(){
      given:
      def javers = JaversBuilder.javers().build()

      def dob = LocalDate.now()
      def p = new Person(name: "n", surname: "s", dob: dob, data: "x")

      when:
      def commit = javers.commit("author", p)
      def snapshot = javers.findSnapshots(QueryBuilder.byInstanceId("n,s,"+dob.toString(), Person).build()).get(0)

      then:
      snapshot.globalId.value() == "Person/n,s,"+dob.toString()
      snapshot.getPropertyValue("name") == "n"
      snapshot.getPropertyValue("surname") == "s"
      snapshot.getPropertyValue("dob") == dob.toString()
      snapshot.getPropertyValue("data") == "x"
    }
}


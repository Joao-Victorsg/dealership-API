package com.example.api.dealership.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_CLIENTMODEL")
public class ClientModel {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true,length = 11)
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private AddressModel address;

    @Column(nullable = false)
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<SalesModel> salesModel;

    public static ClientModel of(ClientModel clientModel, AddressModel addressModel){
        return clientModel.toBuilder().address(addressModel).build();
    }
}

package com.filament.measurement.Authentication.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.filament.measurement.Filament.Model.Filament;
import com.filament.measurement.Filament.Model.FilamentChart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
@Builder
//@ToString(exclude = "users")

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String token;

    @OneToMany(mappedBy = "company",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "company")
    private List<Filament> filaments;
    @OneToMany(mappedBy = "company")
    private List<FilamentChart> filamentsChart;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    @JsonIgnore
    private List<ElectricityTariff> electricityTariffs;


    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getToken() {
        return this.token;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public List<Filament> getFilaments() {
        return this.filaments;
    }

    public List<ElectricityTariff> getElectricityTariffs() {
        return this.electricityTariffs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonIgnore
    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setFilaments(List<Filament> filaments) {
        this.filaments = filaments;
    }

    @JsonIgnore
    public void setElectricityTariffs(List<ElectricityTariff> electricityTariffs) {
        this.electricityTariffs = electricityTariffs;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Company)) return false;
        final Company other = (Company) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) return false;
        final Object this$users = this.getUsers();
        final Object other$users = other.getUsers();
        if (this$users == null ? other$users != null : !this$users.equals(other$users)) return false;
        final Object this$filaments = this.getFilaments();
        final Object other$filaments = other.getFilaments();
        if (this$filaments == null ? other$filaments != null : !this$filaments.equals(other$filaments)) return false;
        final Object this$electricityTariffs = this.getElectricityTariffs();
        final Object other$electricityTariffs = other.getElectricityTariffs();
        if (this$electricityTariffs == null ? other$electricityTariffs != null : !this$electricityTariffs.equals(other$electricityTariffs))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Company;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $token = this.getToken();
        result = result * PRIME + ($token == null ? 43 : $token.hashCode());
        final Object $users = this.getUsers();
        result = result * PRIME + ($users == null ? 43 : $users.hashCode());
        final Object $filaments = this.getFilaments();
        result = result * PRIME + ($filaments == null ? 43 : $filaments.hashCode());
        final Object $electricityTariffs = this.getElectricityTariffs();
        result = result * PRIME + ($electricityTariffs == null ? 43 : $electricityTariffs.hashCode());
        return result;
    }

    public String toString() {
        return "Company(id=" + this.getId() +
                ", name=" + this.getName() +
                ", token=" + this.getToken() +
                ", users=" + this.getUsers() +
                ", electricityTariffs=" + this.getElectricityTariffs() + ")";
    }
}

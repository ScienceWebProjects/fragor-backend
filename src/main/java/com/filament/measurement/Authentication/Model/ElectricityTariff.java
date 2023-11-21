package com.filament.measurement.Authentication.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "electricityTariff")
@Builder
public class ElectricityTariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer hourFrom;
    private Integer hourTo;
    private boolean workingDays;
    private boolean weekend;
    private double price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "company_id"
    )

    @JsonBackReference
    @JsonIgnore
    private Company company;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getHourFrom() {
        return this.hourFrom;
    }

    public Integer getHourTo() {
        return this.hourTo;
    }

    public boolean isWorkingDays() {
        return this.workingDays;
    }

    public boolean isWeekend() {
        return this.weekend;
    }

    public double getPrice() {
        return this.price;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHourFrom(Integer hourFrom) {
        this.hourFrom = hourFrom;
    }

    public void setHourTo(Integer hourTo) {
        this.hourTo = hourTo;
    }

    public void setWorkingDays(boolean workingDays) {
        this.workingDays = workingDays;
    }

    public void setWeekend(boolean weekend) {
        this.weekend = weekend;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @JsonIgnore
    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ElectricityTariff)) return false;
        final ElectricityTariff other = (ElectricityTariff) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$hourFrom = this.getHourFrom();
        final Object other$hourFrom = other.getHourFrom();
        if (this$hourFrom == null ? other$hourFrom != null : !this$hourFrom.equals(other$hourFrom)) return false;
        final Object this$hourTo = this.getHourTo();
        final Object other$hourTo = other.getHourTo();
        if (this$hourTo == null ? other$hourTo != null : !this$hourTo.equals(other$hourTo)) return false;
        if (this.isWorkingDays() != other.isWorkingDays()) return false;
        if (this.isWeekend() != other.isWeekend()) return false;
        if (Double.compare(this.getPrice(), other.getPrice()) != 0) return false;
        final Object this$company = this.getCompany();
        final Object other$company = other.getCompany();
        if (this$company == null ? other$company != null : !this$company.equals(other$company)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ElectricityTariff;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $hourFrom = this.getHourFrom();
        result = result * PRIME + ($hourFrom == null ? 43 : $hourFrom.hashCode());
        final Object $hourTo = this.getHourTo();
        result = result * PRIME + ($hourTo == null ? 43 : $hourTo.hashCode());
        result = result * PRIME + (this.isWorkingDays() ? 79 : 97);
        result = result * PRIME + (this.isWeekend() ? 79 : 97);
        final long $price = Double.doubleToLongBits(this.getPrice());
        result = result * PRIME + (int) ($price >>> 32 ^ $price);
        final Object $company = this.getCompany();
        result = result * PRIME + ($company == null ? 43 : $company.hashCode());
        return result;
    }

    public String toString() {
        return "ElectricityTariff(id=" + this.getId() + "," +
                " name=" + this.getName() + "," +
                " hourFrom=" + this.getHourFrom() +
                ", hourTo=" + this.getHourTo() +
                ", workingDays=" + this.isWorkingDays() +
                ", weekend=" + this.isWeekend() +
                ", price=" + this.getPrice() + ")";
    }
}

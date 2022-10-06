package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.config.AuditingModel;
import com.nta.teabreakorder.enums.Status;
import com.nta.teabreakorder.payload.response.OrderDTO;
import com.nta.teabreakorder.payload.response.TotalBill;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
@SqlResultSetMapping(
        name = "TotalBillMapping",
        classes = {
                @ConstructorResult(
                        targetClass = TotalBill.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "created_at", type = LocalDateTime.class),
                                @ColumnResult(name = "created_by", type = String.class),
                                @ColumnResult(name = "is_deleted", type = Boolean.class),
                                @ColumnResult(name = "status", type = String.class),
                                @ColumnResult(name = "store", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                                @ColumnResult(name = "product", type = String.class),
                                @ColumnResult(name = "quantity", type = Integer.class),
                                @ColumnResult(name = "total", type = BigDecimal.class),
                                @ColumnResult(name = "photo", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                        }),

        })
public class OrderDetail extends AuditingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(name = "status")
    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATED;

    @JsonProperty("description")
    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity")
    private byte quantity;

    @Column(name = "is_paid")
    private boolean isPaid = false;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderDTO orderDTO;


    @OrderBy("id desc")
    @JsonFormat
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_detail_id", referencedColumnName = "id")
    private List<BankingHistory> bankingHistory;

    @PreRemove
    void beforeRemove() {
        System.out.println("PRE REMOVE");
        //this.setOrder(null);
        this.setProduct(null);
        setBankingHistory(null);
    }

    public String getDescription() {
        return description;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public byte getQuantity() {
        return quantity;
    }

    public void setQuantity(byte quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {

        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderDTO getOrderDTO() {
        orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTimeRemaining(order.getTimeRemaining());
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }


    public List<BankingHistory> getBankingHistory() {
        return bankingHistory;
    }

    public void setBankingHistory(List<BankingHistory> bankingHistory) {
        this.bankingHistory = bankingHistory;
    }
}


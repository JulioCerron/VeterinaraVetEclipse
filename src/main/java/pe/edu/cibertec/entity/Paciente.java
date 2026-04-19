package pe.edu.cibertec.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long idPaciente;

    @Column(name = "codigo",  unique = true, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "especie", nullable = false, length = 50)
    private String especie; 

    @Column(name = "raza", length = 50)
    private String raza;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "color", length = 100)
    private String color;

    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "estado")
    private Boolean estado = true;
    
    //relaciona con otras tablas
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties("pacientes") // Evita que el cliente intente listar sus pacientes de nuevo    @ToString.Exclude
    private Cliente cliente;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cita> citas;
}

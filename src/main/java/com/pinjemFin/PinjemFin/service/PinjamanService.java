package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.PengajuanToPeminjamanRequest;
import com.pinjemFin.PinjemFin.models.Pinjaman;
import com.pinjemFin.PinjemFin.models.UsersCustomer;

import com.pinjemFin.PinjemFin.repository.CustomerRepository;
import com.pinjemFin.PinjemFin.repository.PinjamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PinjamanService {
    @Autowired
    private PinjamanRepository pinjamanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final CustomerService customerService;

    public PinjamanService(@Lazy CustomerService customerService) {
        this.customerService = customerService;
    }

    public Pinjaman partialUpdatePinjaman(UUID id, Map<String, Object> updates) {
        Optional<Pinjaman> optionalPinjaman = pinjamanRepository.findById(id);
        if (optionalPinjaman.isEmpty()) {
            throw new RuntimeException("Pinjaman dengan ID " + id + " tidak ditemukan.");
        }

        Pinjaman existingPinjaman = optionalPinjaman.get();
        Class<?> clazz = existingPinjaman.getClass();

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                // Konversi nilai ke tipe data yang sesuai
                if (value != null) {
                    if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                        field.set(existingPinjaman, ((Number) value).doubleValue());
                    } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                        field.set(existingPinjaman, ((Number) value).intValue());
                    } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                        field.set(existingPinjaman, ((Number) value).longValue());
                    } else {
                        field.set(existingPinjaman, value);
                    }
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Field \"" + key + "\" tidak ditemukan pada Pinjaman.");
            } catch (IllegalAccessException | ClassCastException e) {
                throw new RuntimeException("Gagal memperbarui field \"" + key + "\". Tipe data tidak sesuai.");
            }
        }

        return pinjamanRepository.save(existingPinjaman);
    }

    public Pinjaman addPeminjamamn(PengajuanToPeminjamanRequest pengajuan) {
        UsersCustomer usersCustomer = customerRepository.findById(pengajuan.getId_user_customer()).get();
        Pinjaman pinjaman = Pinjaman.builder()
                .jumlah_pinjaman(pengajuan.getAmount())
                .bunga(pengajuan.getBunga())
                .angsuran(pengajuan.getAngsuran())
                .tenor(pengajuan.getTenor())
                .sisa_tenor(pengajuan.getTenor())
                .sisa_pokok_hutang(pengajuan.getAmount())
                .total_payment(pengajuan.getTotal_payment())
                .id_user_customer(usersCustomer)
                .build();
        return pinjamanRepository.save(pinjaman);
    }

    public Double getTotalPeminjamanLunasByUser(String token) {
        String tokenTrimp = token.substring(7); // Hapus "Bearer "
        UUID idUserCustomer = customerService.getUserCustomerIdFromToken(tokenTrimp);

        return pinjamanRepository.getTotalPeminjamanLunasByUser(idUserCustomer);
    }







}

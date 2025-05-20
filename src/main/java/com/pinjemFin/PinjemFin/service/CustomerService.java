package com.pinjemFin.PinjemFin.service;

import com.pinjemFin.PinjemFin.dto.DetailCustomerRequest;
import com.pinjemFin.PinjemFin.dto.InformasiPengajuanResponse;
import com.pinjemFin.PinjemFin.models.Plafon;
import com.pinjemFin.PinjemFin.models.Users;
import com.pinjemFin.PinjemFin.models.UsersCustomer;
import com.pinjemFin.PinjemFin.repository.CustomerRepository;
import com.pinjemFin.PinjemFin.repository.PlafonRepository;
import com.pinjemFin.PinjemFin.repository.UsersRepository;
import com.pinjemFin.PinjemFin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository CustomerRepository;

    @Autowired
    private PlafonRepository plafonRepository;

    @Autowired
    PinjamanService pinjamanService;

    @Autowired
    BranchService branchService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PlafonService plafonService;

    @Autowired
    UserCustomerImageService userCustomerImageService;

    private final JwtUtil jwtUtil;

    @Autowired
    public CustomerService(JwtUtil jwtUtil) {  // Constructor Injection
        this.jwtUtil = jwtUtil;
    }



    @Transactional
    public ResponseEntity<String> cekUpdateAkun(String token) {
        UUID id_user = UUID.fromString(jwtUtil.extractidUser(token));
        Optional<UsersCustomer> usersCustomerOptional = CustomerRepository.findByUsersIdUser(id_user);
        if (userCustomerImageService.hasAllRequiredImages(usersCustomerOptional.get().getId_user_customer())){
            if (usersCustomerOptional.isPresent()) {
                return ResponseEntity.ok("Sudah Melengkapi");
            }
            else {
                return ResponseEntity
                        .status(404) // HTTP 404 Not Found
                        .body("{\"response\":\"Silakan update akun terlebih dahulu\"}");
            }
        }
        else {
            List<String> missingImages = userCustomerImageService.getMissingImages(usersCustomerOptional.get().getId_user_customer());
            String formattedList = missingImages.stream()
                    .map(name -> name.replace("_", " "))  // ubah underscore jadi spasi
                    .collect(Collectors.joining(", "));

            String message = "Anda belum upload " + formattedList;
            return ResponseEntity
                    .status(404) // HTTP 404 Not Found
                    .body(message);
        }


    }

    public UsersCustomer addCustomer(DetailCustomerRequest detailCustomerRequest, String token) {
        UUID userId = UUID.fromString(jwtUtil.extractidUser(token));
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cari dulu apakah user ini sudah punya customer


//        UsersCustomer usersCustomer = getUserCustomer(getUserCustomerIdFromToken(token));
        Optional<UsersCustomer> usersCustomer = CustomerRepository.findByUsersIdUser(userId);
        // Kalau baru, set relasi Users
        if (usersCustomer.isEmpty()) {
            usersCustomer.get().setUsers(users);
            Plafon plafon = plafonService.getplafonbycategory("Bronze");
            usersCustomer.get().setPlafon(plafon);
            usersCustomer.get().setSisa_plafon(plafon.getJumlah_plafon());
        }

        usersCustomer.get().setBranch(branchService.getNearestBranch(
                detailCustomerRequest.getLatitude_alamat(), detailCustomerRequest.getLongitude_alamat()
        ));
        usersCustomer.get().setTempat_tgl_lahir(detailCustomerRequest.getTempat_tgl_lahir());
        usersCustomer.get().setNo_telp(detailCustomerRequest.getNo_telp());
        usersCustomer.get().setAlamat(detailCustomerRequest.getAlamat());
        usersCustomer.get().setNik(detailCustomerRequest.getNik());
        usersCustomer.get().setNama_ibu_kandung(detailCustomerRequest.getNama_ibu_kandung());
        usersCustomer.get().setPekerjaan(detailCustomerRequest.getPekerjaan());
        usersCustomer.get().setGaji(detailCustomerRequest.getGaji());
        usersCustomer.get().setNo_rek(detailCustomerRequest.getNo_rek());
        usersCustomer.get().setStatus_rumah(detailCustomerRequest.getStatus_rumah());

        return CustomerRepository.save(usersCustomer.get());
    }

    public UsersCustomer saveCustomer(UsersCustomer usersCustomer) {
        return CustomerRepository.save(usersCustomer);
    }



    @Transactional
    public UsersCustomer partialUpdate(UUID id, Map<String, Object> updates) {
        return CustomerRepository.findById(id).map(existingCustomer -> {
            updates.forEach((key, value) -> {
                Field field;
                try {
                    field = UsersCustomer.class.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(existingCustomer, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Error updating field: " + key);
                }
            });
            return CustomerRepository.save(existingCustomer);
        }).orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    public UUID getUserCustomerIdFromToken(String token) {
        // Ambil id_user langsung dari token JWT
        UUID idUser = UUID.fromString(jwtUtil.extractidUser(token));

        // Cari id_user_customer berdasarkan id_user
        return CustomerRepository.findByUsersIdUser(idUser)
                .map(UsersCustomer::getId_user_customer)
                .orElseThrow(() -> new RuntimeException("User Customer not found"));
    }

    public UsersCustomer getPlafon(String token) {
        List<Plafon> plafons = plafonRepository.findAllSorted();
        Double jumlPinjLunas = pinjamanService.getTotalPeminjamanLunasByUser("Bearer "+token);
        Plafon plafon = plafons.get(0);
        for (int i = 0; i < plafons.size(); i++){
            if(jumlPinjLunas>plafons.get(i).getJumlah_plafon()){
                plafon = plafons.get(i);
            }
            else{
                break;
            }
        }

        UsersCustomer usersCustomer = CustomerRepository.findById(getUserCustomerIdFromToken(token)).get();
        usersCustomer.setPlafon(plafon);


        return CustomerRepository.save(usersCustomer);
    }

    public UsersCustomer getUserCustomer(UUID id){
        return CustomerRepository.findById(id).get();
    }

    public InformasiPengajuanResponse getInformasiPengajuan(String authHeader) {
        String token = authHeader.substring(7);
        UsersCustomer usersCustomer = getPlafon(token);
        InformasiPengajuanResponse response = new InformasiPengajuanResponse();

        response.setJenis_plafon(usersCustomer.getPlafon().getJenis_plafon());
        response.setJumlah_plafon(usersCustomer.getPlafon().getJumlah_plafon());
        response.setSisa_plafon(usersCustomer.getSisa_plafon());

        Double jumlPinjLunas = pinjamanService.getTotalPeminjamanLunasByUser("Bearer "+token);
        response.setJumlah_pinjamanLunas(jumlPinjLunas);

        Double JumlPinjSaatIni = pinjamanService.getTotalPeminjamanByUser("Bearer "+token)-jumlPinjLunas;
        response.setJumlah_pinjaman(JumlPinjSaatIni);

        List<Plafon> plafons = plafonRepository.findAllSorted();
        Plafon plafonlvUp = plafons.get(0);
        for (int i = 0; i < plafons.size(); i++){
            if(plafons.get(i).getJumlah_plafon().equals(usersCustomer.getPlafon().getJumlah_plafon())){
                plafonlvUp = plafons.get(i+1);
            }
            else{
                break;
            }
        }

        response.setPersentasilvup(jumlPinjLunas/plafonlvUp.getJumlah_plafon());
        return response;



    }
}

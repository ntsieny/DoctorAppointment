package com.bigblackboy.doctorappointment.controller;

import com.bigblackboy.doctorappointment.api.AppointmentListApiResponse;
import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.api.SpecialitiesApiResponse;
import com.bigblackboy.doctorappointment.api.HospitalApiResponse;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HospitalApi {

    @Headers({
            "Host: www.kuban-online.ru",
            "Connection: keep-alive",
            "Content-Length: 307",
            "Accept: */*",
            "Origin: http://www.kuban-online.ru",
            "X-Requested-With: XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
            "DNT: 1",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: http://www.kuban-online.ru/signup/free/",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            "Cookie: _ym_uid=15360594171004491340; _ym_d=1536059417; jv_visits_count_Tmyx3k0Kz1=7; _ym_isad=1"
    })
    @FormUrlEncoded
    @POST("api/clinic_list/")
    Call<HospitalApiResponse> getHospitals(@Field("district_form-district_id") String districtId);

    @Headers({
            "Host: www.kuban-online.ru",
            "Connection: keep-alive",
            "Content-Length: 307",
            "Accept: */*",
            "Origin: http://www.kuban-online.ru",
            "X-Requested-With: XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
            "DNT: 1",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: http://www.kuban-online.ru/signup/free/",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            "Cookie: _ym_uid=15360594171004491340; _ym_d=1551982263; _ym_isad=1; jv_visits_count_Tmyx3k0Kz1=30; sessionid=zaz44uvkmpjhg74jfrx0pi8pz7z89kqe"
            })
    @FormUrlEncoded
    @POST("api/check_patient/")
    Call<CheckPatientApiResponse> getMetadata(@Field("patient_form-first_name") String firstName, @Field("patient_form-last_name") String lastName,
                                              @Field("patient_form-middle_name") String middleName, @Field("patient_form-insurance_series") String insSeries,
                                              @Field("patient_form-insurance_number") String insNumber, @Field("patient_form-birthday") String birthday,
                                              @Field("patient_form-clinic_id") String hospitalId, @Field("csrfmiddlewaretoken") String token);

    @Headers({
            "Host: www.kuban-online.ru",
            "Connection: keep-alive",
            "Content-Length: 307",
            "Accept: */*",
            "Origin: http://www.kuban-online.ru",
            "X-Requested-With: XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
            "DNT: 1",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: http://www.kuban-online.ru/signup/free/",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            "Cookie: _ym_uid=15360594171004491340; _ym_d=1536059417; jv_visits_count_Tmyx3k0Kz1=7; _ym_isad=1"
            })
    @FormUrlEncoded
    @POST("api/speciality_list/")
    Call<SpecialitiesApiResponse> getSpecialities(@Field("clinic_form-clinic_id") String hospitalId, @Field("clinic_form-history_id") String historyId,
                                                  @Field("clinic_form-patientAriaNumber") String patientAriaNumber, @Field("clinic_form-patient_id") String patientId);


    @Headers({
            "Host: www.kuban-online.ru",
            "Connection: keep-alive",
            "Content-Length: 307",
            "Accept: */*",
            "Origin: http://www.kuban-online.ru",
            "X-Requested-With: XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
            "DNT: 1",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: http://www.kuban-online.ru/signup/free/",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            "Cookie: _ym_uid=15360594171004491340; _ym_d=1551982263; _ym_isad=1; jv_visits_count_Tmyx3k0Kz1=30; sessionid=zaz44uvkmpjhg74jfrx0pi8pz7z89kqe"
            })
    @FormUrlEncoded
    @POST("api/doctor_list/")
    Call<DoctorsApiResponse> getDoctors(@Field("speciality_form-speciality_id") String specialityId, @Field("speciality_form-clinic_id") String hospitalId,
                                             @Field("speciality_form-patient_id") String patientId, @Field("speciality_form-history_id") String historyId);


    @Headers({
            "Host: www.kuban-online.ru",
            "Connection: keep-alive",
            "Content-Length: 145",
            "Accept: */*",
            "Origin: http://www.kuban-online.ru",
            "X-Requested-With: XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
            "DNT: 1",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: http://www.kuban-online.ru/signup/free/",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            "Cookie: _ym_uid=15360594171004491340; _ym_d=1536059417; sessionid=zaz44uvkmpjhg74jfrx0pi8pz7z89kqe; _ym_isad=1; jv_enter_ts_Tmyx3k0Kz1=1541604378017; jv_visits_count_Tmyx3k0Kz1=21; jv_utm_Tmyx3k0Kz1=; jv_invitation_time_Tmyx3k0Kz1=1541604399197; jv_close_time_Tmyx3k0Kz1=1541604447645; jv_pages_count_Tmyx3k0Kz1=2"
    })
    @FormUrlEncoded
    @POST("api/appointment_list/")
    Call<AppointmentListApiResponse> getAppointments(@Field("doctor_form-doctor_id") String doctorId, @Field("doctor_form-clinic_id") String hospitalId,
                                       @Field("doctor_form-patient_id") String patientId, @Field("doctor_form-history_id") String historyId,
                                       @Field("doctor_form-appointment_type") String appointmentType);


    @Headers({
            "Host: www.kuban-online.ru",
            "Connection: keep-alive",
            "Content-Length: 145",
            "Accept: */*",
            "Origin: http://www.kuban-online.ru",
            "X-Requested-With: XMLHttpRequest",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
            "DNT: 1",
            "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
            "Referer: http://www.kuban-online.ru/signup/free/",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            "Cookie: _ym_uid=15360594171004491340; _ym_d=1536059417; sessionid=qzuhb1oswdg8kzry3fqsx6cpmudbgkps; _ym_isad=1; jv_enter_ts_Tmyx3k0Kz1=1541604378017; jv_visits_count_Tmyx3k0Kz1=21; jv_utm_Tmyx3k0Kz1=; jv_invitation_time_Tmyx3k0Kz1=1541604399197; jv_close_time_Tmyx3k0Kz1=1541604447645; jv_pages_count_Tmyx3k0Kz1=2"
    })
    @FormUrlEncoded
    @POST("signup/create-appointment/")
    Call<Object> createAppointment(@Field("app_id") String appointmentId, @Field("patient_id") String patientId,
                                                     @Field("lpu_id") String hospitalId, @Field("doctor_form-history_id") String historyId,
                                                     @Field("doctor_form-appointment_type") String appointmentType);
}

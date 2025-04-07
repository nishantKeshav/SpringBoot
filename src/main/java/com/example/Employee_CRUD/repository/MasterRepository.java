package com.example.Employee_CRUD.repository;

import java.util.List;

import com.example.Employee_CRUD.model.Master;
import com.example.Employee_CRUD.dto.projection.BlocksProjection;
import com.example.Employee_CRUD.dto.projection.VillageProjection;
import com.example.Employee_CRUD.dto.projection.MunicipalityProjection;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT block FROM master GROUP BY block ORDER BY block ASC", nativeQuery = true)
    List<BlocksProjection> findAllBlocks();

    @Transactional(readOnly = true)
    @Query(value = "SELECT grampanchayat FROM master WHERE block = :block GROUP BY grampanchayat ORDER BY grampanchayat ASC", nativeQuery = true)
    List<MunicipalityProjection> findGrampanchayatByBlock(@Param("block") String block);

    @Query(value = "select village from master where block = :block and grampanchayat = :grampanchayat group by village order by village asc;" , nativeQuery = true)
    List<VillageProjection> findVillageByBlockGrampanchayat(@Param("block") String block , @Param("grampanchayat") String grampanchayat);
}
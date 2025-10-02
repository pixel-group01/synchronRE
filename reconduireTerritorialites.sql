-- SQL script to replicate the functionality of the reconduireTerritorialites method
-- Parameters:
-- :newTraiteNpId - The ID of the new treaty
-- :traiteNpId - The ID of the original treaty

-- Step 1: Insert new territorialities for the new treaty based on the territorialities of the original treaty
INSERT INTO territorialite (
    terr_id, 
    terr_libelle, 
    terr_taux, 
    terr_description, 
    traite_np_id, 
    sta_code, 
    user_creator, 
    fon_creator, 
    created_at, 
    updated_at
)
SELECT 
    NEXTVAL('TERR_ID_GEN'), 
    t.terr_libelle, 
    t.terr_taux, 
    t.terr_description, 
    :newTraiteNpId, 
    t.sta_code, 
    t.user_creator, 
    t.fon_creator, 
    CURRENT_TIMESTAMP, 
    CURRENT_TIMESTAMP
FROM 
    territorialite t
WHERE 
    t.traite_np_id = :traiteNpId
RETURNING terr_id AS new_terr_id, (
    SELECT terr_id FROM territorialite 
    WHERE traite_np_id = :traiteNpId 
    AND terr_libelle = territorialite.terr_libelle 
    AND terr_taux = territorialite.terr_taux
    LIMIT 1
) AS old_terr_id;

-- Step 2: For each new territoriality, create associations with countries
INSERT INTO association (
    asso_id, 
    pays_code, 
    terr_id, 
    typ_id, 
    sta_code
)
SELECT 
    NEXTVAL('ASSO_ID_GEN'), 
    a.pays_code, 
    nt.new_terr_id, 
    (SELECT type_id FROM type WHERE unique_code = 'TER-DET'), 
    'ACT'
FROM 
    association a
JOIN 
    (SELECT new_terr_id, old_terr_id FROM territorialite_temp) nt ON a.terr_id = nt.old_terr_id
WHERE 
    a.pays_code IS NOT NULL
    AND a.typ_id = (SELECT type_id FROM type WHERE unique_code = 'TER-DET');

-- Step 3: For each new territoriality, create associations with organizations
INSERT INTO association (
    asso_id, 
    organ_code, 
    terr_id, 
    typ_id, 
    sta_code
)
SELECT 
    NEXTVAL('ASSO_ID_GEN'), 
    a.organ_code, 
    nt.new_terr_id, 
    (SELECT type_id FROM type WHERE unique_code = 'TER-DET'), 
    'ACT'
FROM 
    association a
JOIN 
    (SELECT new_terr_id, old_terr_id FROM territorialite_temp) nt ON a.terr_id = nt.old_terr_id
WHERE 
    a.organ_code IS NOT NULL
    AND a.typ_id = (SELECT type_id FROM type WHERE unique_code = 'TER-DET');

-- Note: This script assumes the existence of a temporary table 'territorialite_temp' to store the mapping
-- between old and new territoriality IDs. In a real implementation, you might need to use a different
-- approach depending on your database system's capabilities, such as using a CTE (Common Table Expression)
-- or a temporary table that you create and drop within the script.

-- Alternative implementation using a single transaction with CTEs:

WITH inserted_territorialities AS (
    INSERT INTO territorialite (
        terr_id, 
        terr_libelle, 
        terr_taux, 
        terr_description, 
        traite_np_id, 
        sta_code, 
        user_creator, 
        fon_creator, 
        created_at, 
        updated_at
    )
    SELECT 
        NEXTVAL('TERR_ID_GEN'), 
        t.terr_libelle, 
        t.terr_taux, 
        t.terr_description, 
        :newTraiteNpId, 
        t.sta_code, 
        t.user_creator, 
        t.fon_creator, 
        CURRENT_TIMESTAMP, 
        CURRENT_TIMESTAMP
    FROM 
        territorialite t
    WHERE 
        t.traite_np_id = :traiteNpId
    RETURNING terr_id AS new_terr_id, terr_libelle, terr_taux
),
old_territorialities AS (
    SELECT 
        t.terr_id AS old_terr_id,
        t.terr_libelle,
        t.terr_taux
    FROM 
        territorialite t
    WHERE 
        t.traite_np_id = :traiteNpId
),
territoriality_mapping AS (
    SELECT 
        i.new_terr_id,
        o.old_terr_id
    FROM 
        inserted_territorialities i
    JOIN 
        old_territorialities o ON i.terr_libelle = o.terr_libelle AND i.terr_taux = o.terr_taux
),
country_associations AS (
    INSERT INTO association (
        asso_id, 
        pays_code, 
        terr_id, 
        typ_id, 
        sta_code
    )
    SELECT 
        NEXTVAL('ASSO_ID_GEN'), 
        a.pays_code, 
        tm.new_terr_id, 
        (SELECT type_id FROM type WHERE unique_code = 'TER-DET'), 
        'ACT'
    FROM 
        association a
    JOIN 
        territoriality_mapping tm ON a.terr_id = tm.old_terr_id
    WHERE 
        a.pays_code IS NOT NULL
        AND a.typ_id = (SELECT type_id FROM type WHERE unique_code = 'TER-DET')
    RETURNING asso_id
)
INSERT INTO association (
    asso_id, 
    organ_code, 
    terr_id, 
    typ_id, 
    sta_code
)
SELECT 
    NEXTVAL('ASSO_ID_GEN'), 
    a.organ_code, 
    tm.new_terr_id, 
    (SELECT type_id FROM type WHERE unique_code = 'TER-DET'), 
    'ACT'
FROM 
    association a
JOIN 
    territoriality_mapping tm ON a.terr_id = tm.old_terr_id
WHERE 
    a.organ_code IS NOT NULL
    AND a.typ_id = (SELECT type_id FROM type WHERE unique_code = 'TER-DET');
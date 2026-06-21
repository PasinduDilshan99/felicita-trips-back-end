package com.felicita.queries;

public class BookingQueries {

    public static final String GET_COMPLETE_BOOKING_DETAILS_BY_ID = """
            SELECT 
                b.booking_id,
                b.booking_reference,
                b.booking_date,
                b.travel_start_date,
                b.travel_end_date,
                b.total_persons,
                b.total_amount,
                b.discount_amount,
                b.tax_amount,
                b.insurance_amount,
                b.final_amount,
                bs.name AS booking_status,
                cr.name AS cancellation_reason,
                b.cancellation_date,
                b.refund_amount,
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.start_location,
                t.end_location,
                -- tt.name AS tour_type,
                -- tc.name AS tour_category,
                p.name AS package_name,
                p.description AS package_description,
                p.total_price AS package_total_price,
                p.discount_percentage,
                p.price_per_person AS package_price_per_person,
                ps.name AS package_schedule_name,
                ps.assume_start_date,
                ps.assume_end_date,
                u.username,
                CONCAT(u.first_name, ' ', u.last_name) AS user_full_name,
                u.email,
                u.mobile_number1,
                DATEDIFF(b.travel_end_date, b.travel_start_date) + 1 AS actual_duration_days,
                CONCAT('Completed ', DATEDIFF(CURDATE(), b.travel_end_date), ' days ago') AS completion_time
            FROM bookings b
            LEFT JOIN booking_status bs ON b.booking_status_id = bs.id
            LEFT JOIN package_schedule ps ON b.package_schedule_id = ps.id
            LEFT JOIN packages p ON b.package_id = p.package_id
            LEFT JOIN tour t ON b.tour_id = t.tour_id
            -- LEFT JOIN tour_type tt ON t.tour_type = tt.id
            -- LEFT JOIN tour_category tc ON t.tour_category = tc.id
            LEFT JOIN cancellation_reasons cr ON b.cancellation_reason_id = cr.id
            LEFT JOIN user u ON b.user_id = u.user_id
            WHERE b.user_id = ?
            AND bs.name = 'TOUR_COMPLETED'
            ORDER BY b.travel_end_date DESC
            """;

    public static final String GET_COMPLETE_BOOKING_PARTICIPANTS = """
            SELECT 
                bp.booking_id,
                bp.first_name,
                bp.last_name,
                bp.date_of_birth,
                TIMESTAMPDIFF(YEAR, bp.date_of_birth, CURDATE()) AS age,
                g.name AS gender,
                bp.passport_number,
                c.name AS nationality,
                bp.email,
                bp.mobile_number,
                bp.emergency_contact_name,
                bp.emergency_contact_phone,
                bp.emergency_contact_relationship,
                bp.medical_conditions,
                bp.allergies,
                bp.special_assistance_required,
                bp.assistance_details,
                bp2.first_name AS room_sharing_with_first_name,
                bp2.last_name AS room_sharing_with_last_name
            FROM booking_participants bp
            LEFT JOIN gender g ON bp.gender_id = g.gender_id
            LEFT JOIN country c ON bp.nationality_country_id = c.country_id
            LEFT JOIN booking_participants bp2 ON bp.room_sharing_with = bp2.id
            WHERE bp.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? AND bs.name = 'TOUR_COMPLETED'
            )
            ORDER BY bp.booking_id, bp.id
            """;

    // Similarly define the other queries...
    public static final String GET_COMPLETE_BOOKING_ACTIVITIES = """
            SELECT
                ba.booking_id,
                a.name AS activity_name,
                a.description AS activity_description,
                -- ac.name AS activity_category,
                ba.activity_date,
                ba.start_time,
                ba.end_time,
                ba.number_of_participants,
                ba.price_per_person,
                ba.total_price,
                d.name AS destination_name,
                a.duration_hours,
                a.price_local,
                a.price_foreigners,
                -- Activity status (completed since tour is completed)
                'COMPLETED' AS activity_status
            FROM booking_activities ba
            LEFT JOIN activities a ON ba.activity_id = a.id
            -- LEFT JOIN activity_category ac ON a.activities_category = ac.id
            LEFT JOIN destination d ON a.destination_id = d.destination_id
            WHERE ba.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? AND bs.name = 'TOUR_COMPLETED'
            )
            ORDER BY ba.booking_id, ba.activity_date, ba.start_time
            """;
    public static final String GET_COMPLETE_BOOKING_PAYMENTS = """
            SELECT
                p.booking_id,
                p.payment_reference,
                p.amount,
                pm.name AS payment_method,
                ps.name AS payment_status,
                p.installment_number,
                p.total_installments,
                p.payment_date,
                p.due_date,
                p.transaction_id,
                bi.invoice_number,
                bi.invoice_date,
                bi.total_amount AS invoice_total,
                bi.amount_paid,
                bi.balance_due
            FROM payments p
            INNER JOIN payment_methods pm ON p.payment_method_id = pm.id
            INNER JOIN payment_status ps ON p.payment_status_id = ps.id
            LEFT JOIN booking_invoices bi ON p.booking_id = bi.booking_id
            WHERE p.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? AND bs.name = 'TOUR_COMPLETED'
            )
            ORDER BY p.booking_id, p.installment_number
            """;
    public static final String GET_COMPLETE_BOOKING_DOCUMENTS = """
            SELECT
                bd.booking_id,
                bd.document_type,
                bd.document_name,
                bd.document_url,
                bd.file_size
            FROM booking_documents bd
            WHERE bd.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? AND bs.name = 'TOUR_COMPLETED'
            )
            ORDER BY bd.booking_id, bd.document_type
            """;


    public static final String GET_UPCOMING_BOOKING_DETAILS_BY_ID = """
            SELECT 
                b.booking_id,
                b.booking_reference,
                b.booking_date,
                b.travel_start_date,
                b.travel_end_date,
                b.total_persons,
                b.total_amount,
                b.discount_amount,
                b.tax_amount,
                b.insurance_amount,
                b.final_amount,
                bs.name AS booking_status,
                cr.name AS cancellation_reason,
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.start_location,
                t.end_location,
                -- tt.name AS tour_type,
                -- tc.name AS tour_category,
                p.name AS package_name,
                p.description AS package_description,
                p.total_price AS package_total_price,
                p.discount_percentage,
                p.price_per_person AS package_price_per_person,
                ps.name AS package_schedule_name,
                ps.assume_start_date,
                ps.assume_end_date,
                u.username,
                CONCAT(u.first_name, ' ', u.last_name) AS user_full_name,
                u2.user_id AS assign_to,
                CONCAT(u2.first_name, ' ', u2.last_name) AS assign_to_name,
                u.email,
                u.mobile_number1,
                DATEDIFF(b.travel_start_date, CURDATE()) AS days_until_travel,
                CASE 
                    WHEN DATEDIFF(b.travel_start_date, CURDATE()) <= 7 THEN 'IMMINENT'
                    WHEN DATEDIFF(b.travel_start_date, CURDATE()) <= 30 THEN 'SOON'
                    ELSE 'FUTURE'
                END AS travel_urgency,
                CONCAT('Starts in ', DATEDIFF(b.travel_start_date, CURDATE()), ' days') AS countdown
            FROM bookings b
            LEFT JOIN booking_status bs ON b.booking_status_id = bs.id
            LEFT JOIN package_schedule ps ON b.package_schedule_id = ps.id
            LEFT JOIN packages p ON b.package_id = p.package_id
            LEFT JOIN tour t ON b.tour_id = t.tour_id
            -- LEFT JOIN tour_type tt ON t.tour_type = tt.id
            -- LEFT JOIN tour_category tc ON t.tour_category = tc.id
            LEFT JOIN cancellation_reasons cr ON b.cancellation_reason_id = cr.id
            LEFT JOIN user u ON b.user_id = u.user_id
            LEFT JOIN user u2 ON b.assign_to = u.user_id
            WHERE b.user_id = ?
            AND bs.name IN ('CONFIRMED', 'PAID')
            AND b.travel_start_date > CURDATE()
            ORDER BY b.travel_start_date ASC
            """;

    public static final String GET_UPCOMING_BOOKING_PARTICIPANTS = """
            SELECT 
                bp.booking_id,
                bp.first_name,
                bp.last_name,
                bp.date_of_birth,
                TIMESTAMPDIFF(YEAR, bp.date_of_birth, CURDATE()) AS age,
                g.name AS gender,
                bp.passport_number,
                c.name AS nationality,
                bp.email,
                bp.mobile_number,
                bp.emergency_contact_name,
                bp.emergency_contact_phone,
                bp.emergency_contact_relationship,
                bp.medical_conditions,
                bp.allergies,
                bp.special_assistance_required,
                bp.assistance_details,
                bp2.first_name AS room_sharing_with_first_name,
                bp2.last_name AS room_sharing_with_last_name
            FROM booking_participants bp
            LEFT JOIN gender g ON bp.gender_id = g.gender_id
            LEFT JOIN country c ON bp.nationality_country_id = c.country_id
            LEFT JOIN booking_participants bp2 ON bp.room_sharing_with = bp2.id
            WHERE bp.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('CONFIRMED', 'PAID')
                AND b.travel_start_date > CURDATE()
            )
            ORDER BY bp.booking_id, bp.id
            """;

    public static final String GET_UPCOMING_BOOKING_ACTIVITIES = """
            SELECT 
                ba.booking_id,
                a.name AS activity_name,
                a.description AS activity_description,
                -- ac.name AS activity_category,
                ba.activity_date,
                ba.start_time,
                ba.end_time,
                ba.number_of_participants,
                ba.price_per_person,
                ba.total_price,
                d.name AS destination_name,
                a.duration_hours,
                a.price_local,
                a.price_foreigners,
                DATEDIFF(ba.activity_date, CURDATE()) AS days_until_activity,
                CASE 
                    WHEN ba.activity_date = b.travel_start_date THEN 'FIRST_DAY'
                    WHEN ba.activity_date = b.travel_end_date THEN 'LAST_DAY'
                    ELSE 'MID_TOUR'
                END AS activity_timing
            FROM booking_activities ba
            INNER JOIN activities a ON ba.activity_id = a.id
            INNER JOIN bookings b ON ba.booking_id = b.booking_id
            -- LEFT JOIN activity_category ac ON a.activities_category = ac.id
            LEFT JOIN destination d ON a.destination_id = d.destination_id
            WHERE ba.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('CONFIRMED', 'PAID')
                AND b.travel_start_date > CURDATE()
            )
            ORDER BY ba.booking_id, ba.activity_date, ba.start_time
            """;

    public static final String GET_UPCOMING_BOOKING_PAYMENTS = """
            SELECT 
                p.booking_id,
                p.payment_reference,
                p.amount,
                pm.name AS payment_method,
                ps.name AS payment_status,
                p.installment_number,
                p.total_installments,
                p.payment_date,
                p.due_date,
                p.transaction_id,
                bi.invoice_number,
                bi.invoice_date,
                bi.total_amount AS invoice_total,
                bi.amount_paid,
                bi.balance_due,
                CASE 
                    WHEN p.due_date <= CURDATE() AND ps.name != 'COMPLETED' THEN 'OVERDUE'
                    WHEN p.due_date <= CURDATE() + INTERVAL 7 DAY AND ps.name != 'COMPLETED' THEN 'DUE_SOON'
                    ELSE 'UP_TO_DATE'
                END AS payment_urgency
            FROM payments p
            INNER JOIN payment_methods pm ON p.payment_method_id = pm.id
            INNER JOIN payment_status ps ON p.payment_status_id = ps.id
            LEFT JOIN booking_invoices bi ON p.booking_id = bi.booking_id
            WHERE p.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('CONFIRMED', 'PAID')
                AND b.travel_start_date > CURDATE()
            )
            ORDER BY p.booking_id, p.installment_number
            """;

    public static final String GET_UPCOMING_BOOKING_DOCUMENTS = """
            SELECT 
                bd.booking_id,
                bd.document_type,
                bd.document_name,
                bd.document_url,
                bd.file_size,
                CASE 
                    WHEN bd.document_type IN ('ITINERARY', 'TICKET') THEN 'TRAVEL_DOCS'
                    WHEN bd.document_type IN ('INVOICE', 'RECEIPT') THEN 'FINANCIAL_DOCS'
                    ELSE 'OTHER_DOCS'
                END AS document_category
            FROM booking_documents bd
            WHERE bd.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('CONFIRMED', 'PAID')
                AND b.travel_start_date > CURDATE()
            )
            ORDER BY bd.booking_id, bd.document_type
            """;

    public static final String GET_REQUESTED_BOOKING_DETAILS_BY_ID = """
            SELECT
                b.booking_id,
                b.booking_reference,
                b.booking_date,
                b.travel_start_date,
                b.travel_end_date,
                b.total_persons,
                b.total_amount,
                b.discount_amount,
                b.tax_amount,
                b.insurance_amount,
                b.final_amount,
                bs.name AS booking_status,
                cr.name AS cancellation_reason,
                b.cancellation_date,
                b.refund_amount,
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.start_location,
                t.end_location,
                -- tt.name AS tour_type,
                -- tc.name AS tour_category,
                p.name AS package_name,
                p.description AS package_description,
                p.total_price AS package_total_price,
                p.discount_percentage,
                p.price_per_person AS package_price_per_person,
                ps.name AS package_schedule_name,
                ps.assume_start_date,
                ps.assume_end_date,
                u.username,
                CONCAT(u.first_name, ' ', u.last_name) AS user_full_name,
                u2.user_id AS assign_to,
                CONCAT(u2.first_name, ' ', u2.last_name) AS assign_to_name,
                u.email,
                u.mobile_number1,
                -- Request Status Info
                CASE 
                    WHEN bs.name = 'PENDING' THEN 'AWAITING_APPROVAL'
                    WHEN bs.name = 'IN_PROGRESS' THEN 'UNDER_REVIEW'
                    ELSE 'PROCESSING'
                END AS request_status,
                CASE 
                    WHEN bs.name = 'PENDING' THEN 'PENDING_APPROVAL'
                    WHEN bs.name = 'IN_PROGRESS' THEN 'IN_REVIEW'
                    ELSE 'PROCESSING'
                END AS approval_status,
                DATEDIFF(b.travel_start_date, CURDATE()) AS days_until_travel,
                CASE 
                    WHEN DATEDIFF(b.travel_start_date, CURDATE()) <= 7 THEN 'URGENT'
                    WHEN DATEDIFF(b.travel_start_date, CURDATE()) <= 30 THEN 'SOON'
                    ELSE 'FUTURE'
                END AS request_urgency,
                CONCAT('Requested ', DATEDIFF(CURDATE(), b.booking_date), ' days ago') AS request_age
            FROM bookings b
            LEFT JOIN booking_status bs ON b.booking_status_id = bs.id
            LEFT JOIN package_schedule ps ON b.package_schedule_id = ps.id
            LEFT JOIN packages p ON b.package_id = p.package_id
            LEFT JOIN tour t ON b.tour_id = t.tour_id
            -- LEFT JOIN tour_type tt ON t.tour_type = tt.id
            -- LEFT JOIN tour_category tc ON t.tour_category = tc.id
            LEFT JOIN cancellation_reasons cr ON b.cancellation_reason_id = cr.id
            LEFT JOIN user u ON b.user_id = u.user_id
            LEFT JOIN user u2 ON u2.user_id = b.assign_to
            WHERE b.user_id = ?
            AND bs.name IN ('PENDING', 'IN_PROGRESS')
            ORDER BY 
                CASE 
                    WHEN bs.name = 'PENDING' THEN 1
                    WHEN bs.name = 'IN_PROGRESS' THEN 2
                    ELSE 3
                END,
                b.booking_date DESC
            """;

    public static final String GET_REQUESTED_BOOKING_PARTICIPANTS = """
            SELECT 
                bp.booking_id,
                bp.first_name,
                bp.last_name,
                bp.date_of_birth,
                TIMESTAMPDIFF(YEAR, bp.date_of_birth, CURDATE()) AS age,
                g.name AS gender,
                bp.passport_number,
                c.name AS nationality,
                bp.email,
                bp.mobile_number,
                bp.emergency_contact_name,
                bp.emergency_contact_phone,
                bp.emergency_contact_relationship,
                bp.medical_conditions,
                bp.allergies,
                bp.special_assistance_required,
                bp.assistance_details,
                bp2.first_name AS room_sharing_with_first_name,
                bp2.last_name AS room_sharing_with_last_name
            FROM booking_participants bp
            LEFT JOIN gender g ON bp.gender_id = g.gender_id
            LEFT JOIN country c ON bp.nationality_country_id = c.country_id
            LEFT JOIN booking_participants bp2 ON bp.room_sharing_with = bp2.id
            WHERE bp.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('PENDING', 'IN_PROGRESS')
            )
            ORDER BY bp.booking_id, bp.id
            """;

    public static final String GET_REQUESTED_BOOKING_ACTIVITIES = """
            SELECT 
                ba.booking_id,
                a.name AS activity_name,
                a.description AS activity_description,
                -- ac.name AS activity_category,
                ba.activity_date,
                ba.start_time,
                ba.end_time,
                ba.number_of_participants,
                ba.price_per_person,
                ba.total_price,
                d.name AS destination_name,
                a.duration_hours,
                a.price_local,
                a.price_foreigners,
                'REQUESTED' AS activity_status,
                CASE 
                    WHEN a.max_participate IS NOT NULL AND ba.number_of_participants > a.max_participate THEN 'WAITLIST'
                    ELSE 'AVAILABLE'
                END AS availability_status
            FROM booking_activities ba
            INNER JOIN activities a ON ba.activity_id = a.id
            -- LEFT JOIN activity_category ac ON a.activities_category = ac.id
            LEFT JOIN destination d ON a.destination_id = d.destination_id
            WHERE ba.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('PENDING', 'IN_PROGRESS')
            )
            ORDER BY ba.booking_id, ba.activity_date, ba.start_time
            """;

    public static final String GET_REQUESTED_BOOKING_PAYMENTS = """
            SELECT 
                p.booking_id,
                p.payment_reference,
                p.amount,
                pm.name AS payment_method,
                ps.name AS payment_status,
                p.installment_number,
                p.total_installments,
                p.payment_date,
                p.due_date,
                p.transaction_id,
                bi.invoice_number,
                bi.invoice_date,
                bi.total_amount AS invoice_total,
                bi.amount_paid,
                bi.balance_due,
                CASE 
                    WHEN p.due_date <= CURDATE() AND ps.name != 'COMPLETED' THEN 'HIGH_PRIORITY'
                    WHEN p.due_date <= CURDATE() + INTERVAL 7 DAY AND ps.name != 'COMPLETED' THEN 'MEDIUM_PRIORITY'
                    ELSE 'LOW_PRIORITY'
                END AS payment_priority,
                CASE 
                    WHEN p.amount > 1000 THEN TRUE
                    ELSE FALSE
                END AS deposit_required,
                CASE 
                    WHEN p.amount > 1000 THEN p.amount * 0.2
                    ELSE p.amount
                END AS deposit_amount
            FROM payments p
            INNER JOIN payment_methods pm ON p.payment_method_id = pm.id
            INNER JOIN payment_status ps ON p.payment_status_id = ps.id
            LEFT JOIN booking_invoices bi ON p.booking_id = bi.booking_id
            WHERE p.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('PENDING', 'IN_PROGRESS')
            )
            ORDER BY p.booking_id, p.installment_number
            """;

    public static final String GET_REQUESTED_BOOKING_DOCUMENTS = """
            SELECT 
                bd.booking_id,
                bd.document_type,
                bd.document_name,
                bd.document_url,
                bd.file_size,
                CASE 
                    WHEN bd.document_type IN ('PASSPORT_COPY', 'ID_PROOF') THEN 'VERIFIED'
                    ELSE 'PENDING_VERIFICATION'
                END AS document_status,
                CASE 
                    WHEN bd.document_type IN ('PASSPORT_COPY', 'ID_PROOF', 'MEDICAL_CERTIFICATE') THEN TRUE
                    ELSE FALSE
                END AS required_for_approval
            FROM booking_documents bd
            WHERE bd.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name IN ('PENDING', 'IN_PROGRESS')
            )
            ORDER BY bd.booking_id, bd.document_type
            """;

    public static final String GET_CANCELLED_BOOKING_DETAILS_BY_ID = """
            SELECT
                b.booking_id,
                b.booking_reference,
                b.booking_date,
                b.travel_start_date,
                b.travel_end_date,
                b.total_persons,
                b.total_amount,
                b.discount_amount,
                b.tax_amount,
                b.insurance_amount,
                b.final_amount,
                bs.name AS booking_status,
                cr.name AS cancellation_reason,
                b.cancellation_date,
                b.refund_amount,
                COALESCE(rs.name, 'PENDING') AS refund_status,
                COALESCE(r.refund_amount, 0.00) AS refunded_amount,
                r.processed_date AS refund_processed_date,
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.start_location,
                t.end_location,
                -- tt.name AS tour_type,
                -- tc.name AS tour_category,
                p.name AS package_name,
                p.description AS package_description,
                p.total_price AS package_total_price,
                p.discount_percentage,
                p.price_per_person AS package_price_per_person,
                ps.name AS package_schedule_name,
                ps.assume_start_date,
                ps.assume_end_date,
                u.username,
                CONCAT(u.first_name, ' ', u.last_name) AS user_full_name,
                u.email,
                u.mobile_number1,
                -- Cancellation Info
                CASE 
                    WHEN DATEDIFF(b.cancellation_date, b.booking_date) < 7 THEN 'EARLY_CANCELLATION'
                    WHEN DATEDIFF(b.travel_start_date, b.cancellation_date) > 30 THEN 'ADVANCED_CANCELLATION'
                    WHEN DATEDIFF(b.travel_start_date, b.cancellation_date) > 7 THEN 'LAST_MINUTE_CANCELLATION'
                    ELSE 'LATE_CANCELLATION'
                END AS cancellation_stage,
                DATEDIFF(b.travel_start_date, DATE(b.cancellation_date)) AS days_before_travel,
                b.final_amount * 0.1 AS cancellation_fee,
                CASE 
                    WHEN DATEDIFF(b.travel_start_date, DATE(b.cancellation_date)) > 30 THEN 10.00
                    WHEN DATEDIFF(b.travel_start_date, DATE(b.cancellation_date)) > 14 THEN 25.00
                    WHEN DATEDIFF(b.travel_start_date, DATE(b.cancellation_date)) > 7 THEN 50.00
                    ELSE 75.00
                END AS cancellation_penalty_percentage,
                b.cancellation_notes
            FROM bookings b
            LEFT JOIN booking_status bs ON b.booking_status_id = bs.id
            LEFT JOIN package_schedule ps ON b.package_schedule_id = ps.id
            LEFT JOIN packages p ON b.package_id = p.package_id
            LEFT JOIN tour t ON b.tour_id = t.tour_id
            -- LEFT JOIN tour_type tt ON t.tour_type = tt.id
            -- LEFT JOIN tour_category tc ON t.tour_category = tc.id
            LEFT JOIN cancellation_reasons cr ON b.cancellation_reason_id = cr.id
            LEFT JOIN refunds r ON b.booking_id = r.booking_id
            LEFT JOIN refund_status rs ON r.refund_status_id = rs.id
            LEFT JOIN user u ON b.user_id = u.user_id
            WHERE b.user_id = ?
            AND bs.name = 'CANCELLED'
            ORDER BY b.cancellation_date DESC
            """;

    public static final String GET_CANCELLED_BOOKING_PARTICIPANTS = """
            SELECT 
                bp.booking_id,
                bp.first_name,
                bp.last_name,
                bp.date_of_birth,
                TIMESTAMPDIFF(YEAR, bp.date_of_birth, CURDATE()) AS age,
                g.name AS gender,
                bp.passport_number,
                c.name AS nationality,
                bp.email,
                bp.mobile_number,
                bp.emergency_contact_name,
                bp.emergency_contact_phone,
                bp.emergency_contact_relationship,
                bp.medical_conditions,
                bp.allergies,
                bp.special_assistance_required,
                bp.assistance_details,
                bp2.first_name AS room_sharing_with_first_name,
                bp2.last_name AS room_sharing_with_last_name,
                CASE 
                    WHEN r.refund_id IS NOT NULL THEN TRUE
                    ELSE FALSE
                END AS refund_issued,
                COALESCE(r.refund_amount / (
                    SELECT COUNT(*) 
                    FROM booking_participants bp3 
                    WHERE bp3.booking_id = bp.booking_id
                ), 0.00) AS participant_refund_amount
            FROM booking_participants bp
            LEFT JOIN gender g ON bp.gender_id = g.gender_id
            LEFT JOIN country c ON bp.nationality_country_id = c.country_id
            LEFT JOIN booking_participants bp2 ON bp.room_sharing_with = bp2.id
            LEFT JOIN refunds r ON bp.booking_id = r.booking_id
            WHERE bp.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name = 'CANCELLED'
            )
            ORDER BY bp.booking_id, bp.id
            """;

    public static final String GET_CANCELLED_BOOKING_ACTIVITIES = """
            SELECT 
                ba.booking_id,
                a.name AS activity_name,
                a.description AS activity_description,
                -- ac.name AS activity_category,
                ba.activity_date,
                ba.start_time,
                ba.end_time,
                ba.number_of_participants,
                ba.price_per_person,  -- FIXED: Changed from price_perperson to price_per_person
                ba.total_price,
                d.name AS destination_name,
                a.duration_hours,
                a.price_local,
                a.price_foreigners,
                'CANCELLED' AS activity_status,
                CASE 
                    WHEN DATEDIFF(DATE(b.cancellation_date), ba.activity_date) > 7 THEN TRUE
                    ELSE FALSE
                END AS activity_refundable,
                CASE 
                    WHEN DATEDIFF(DATE(b.cancellation_date), ba.activity_date) > 7 THEN ba.total_price * 0.8
                    ELSE 0.00
                END AS activity_refund_amount
            FROM booking_activities ba
            INNER JOIN activities a ON ba.activity_id = a.id
            INNER JOIN bookings b ON ba.booking_id = b.booking_id
            -- LEFT JOIN activity_category ac ON a.activities_category = ac.id
            LEFT JOIN destination d ON a.destination_id = d.destination_id
            WHERE ba.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name = 'CANCELLED'
            )
            ORDER BY ba.booking_id, ba.activity_date, ba.start_time
            """;

    public static final String GET_CANCELLED_BOOKING_PAYMENTS = """
            SELECT 
                p.booking_id,
                p.payment_reference,
                p.amount,
                pm.name AS payment_method,
                ps.name AS payment_status,
                p.installment_number,
                p.total_installments,
                p.payment_date,
                p.due_date,
                p.transaction_id,
                bi.invoice_number,
                bi.invoice_date,
                bi.total_amount AS invoice_total,
                bi.amount_paid,
                bi.balance_due,
            
                -- Refund Information (FIXED: Use consistent naming)
                r.refund_reference,
                r.refund_amount,
                r.processed_date AS refund_date,
                rs.name AS refund_status,  -- FIXED: Changed back to refund_status
            
                -- Payment Priority
                CASE 
                    WHEN p.due_date <= CURDATE() AND ps.name != 'COMPLETED' THEN 'HIGH_PRIORITY'
                    WHEN p.due_date <= CURDATE() + INTERVAL 7 DAY AND ps.name != 'COMPLETED' THEN 'MEDIUM_PRIORITY'
                    ELSE 'LOW_PRIORITY'
                END AS payment_priority,
            
                -- Deposit Info
                CASE 
                    WHEN p.amount > 1000 THEN TRUE
                    ELSE FALSE
                END AS deposit_required,
            
                CASE 
                    WHEN p.amount > 1000 THEN p.amount * 0.2
                    ELSE p.amount
                END AS deposit_amount,
            
                -- Cancellation Refund Info (FIXED: This was duplicate, remove or rename)
                CASE 
                    WHEN r.refund_id IS NOT NULL THEN 'REFUND_PROCESSED'
                    WHEN b.refund_amount > 0 THEN 'REFUND_PENDING'
                    ELSE 'NO_REFUND'
                END AS refund_status_info,
            
                -- Add payment priority fields that were missing in Response
                p.payment_method_id,
                p.payment_status_id,
                r.refund_status_id
            
            FROM payments p
            INNER JOIN payment_methods pm ON p.payment_method_id = pm.id
            INNER JOIN payment_status ps ON p.payment_status_id = ps.id
            LEFT JOIN booking_invoices bi ON p.booking_id = bi.booking_id
            LEFT JOIN refunds r ON p.booking_id = r.booking_id AND p.payment_id = r.payment_id
            LEFT JOIN refund_status rs ON r.refund_status_id = rs.id
            LEFT JOIN bookings b ON p.booking_id = b.booking_id
            WHERE p.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name = 'CANCELLED'
            )
            ORDER BY p.booking_id, p.installment_number
            """;

    public static final String GET_CANCELLED_BOOKING_DOCUMENTS = """
            SELECT 
                bd.booking_id,
                bd.document_type,
                bd.document_name,
                bd.document_url,
                bd.file_size,
                CASE 
                    WHEN bd.document_type IN ('CANCELLATION_FORM', 'REFUND_REQUEST') THEN 'PROCESSED'
                    ELSE 'ARCHIVED'
                END AS document_status,
                CASE 
                    WHEN bd.document_type IN ('CANCELLATION_FORM', 'REFUND_REQUEST', 'CANCELLATION_CONFIRMATION') THEN TRUE
                    ELSE FALSE
                END AS cancellation_related
            FROM booking_documents bd
            WHERE bd.booking_id IN (
                SELECT b.booking_id FROM bookings b
                INNER JOIN booking_status bs ON b.booking_status_id = bs.id
                WHERE b.user_id = ? 
                AND bs.name = 'CANCELLED'
            )
            ORDER BY bd.booking_id, bd.document_type
            """;

    public static final String INSERT_BOOKING_BASIC_DETAILS = """
                INSERT INTO bookings (
                    booking_reference,
                    user_id,
                    package_schedule_id,
                    total_persons,
                    total_amount,
                    discount_amount,
                    tax_amount,
                    insurance_amount,
                    final_amount,
                    booking_date,
                    travel_start_date,
                    travel_end_date,
                    booking_status_id,
                    special_requirements,
                    dietary_restrictions,
                    insurance_required,
                    created_by
                ) VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                )
            """;

    public static final String INSERT_BOOKING_TRANSPORTATION = """
                INSERT INTO booking_transportation (
                    booking_id,
                    transport_type,
                    departure_date,
                    carrier_name,
                    reference_number,
                    created_at,
                    created_by
                ) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)
            """;

    public static final String INSERT_BOOKING_PRICE_BREAKDOWN = """
                INSERT INTO booking_price_breakdown (
                    booking_id,
                    item_type,
                    item_name,
                    item_description,
            		quantity,
                    unit_price,
                    total_price,
                    created_at,
                    created_by
                ) VALUES (?, ?, ?, ?, ?, ?,?, CURRENT_TIMESTAMP, ?)
            """;

    public static final String INSERT_BOOKING_PARTICIPANT = """
                INSERT INTO booking_participants (
                    booking_id,
                    first_name,
                    last_name,
                    date_of_birth,
                    gender_id,
                    passport_number,
                    nationality_country_id,
                    email,
                    mobile_number,
                    emergency_contact_name,
                    emergency_contact_phone,
                    emergency_contact_relationship,
                    medical_conditions,
                    allergies,
                    special_assistance_required,
                    assistance_details,
                    room_sharing_with,
                    created_by
                ) VALUES (
                    ?, -- booking_id
                    ?, -- first_name
                    ?, -- last_name
                    ?, -- date_of_birth
                    (SELECT gender_id FROM gender WHERE name = ? LIMIT 1),        -- gender_id from gender table
                    ?, -- passport_number
                    (SELECT country_id FROM country WHERE name = ? LIMIT 1),      -- nationality_country_id from country table
                    ?, -- email
                    ?, -- mobile_number
                    ?, -- emergency_contact_name
                    ?, -- emergency_contact_phone
                    ?, -- emergency_contact_relationship
                    ?, -- medical_conditions
                    ?, -- allergies
                    ?, -- special_assistance_required
                    ?, -- assistance_details
                    ?, -- room_sharing_with
                    ?  -- created_by
                )
            """;


    public static final String INSERT_BOOKING_NOTE = """
                INSERT INTO booking_notes (
                    booking_id,
                    note_type,
                    note_text,
                    created_at,
                    created_by
                ) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)
            """;

    public static final String INSERT_BOOKING_ITINERARY = """
                INSERT INTO booking_itinerary (
                    booking_id,
                    day_number,
                    itinerary_date,
                    title,
                    description,
                    start_time,
                    end_time,
                    location,
                    included_meals,
                    created_at,
                    created_by
                ) VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?
                )
            """;

    public static final String INSERT_BOOKING_ACTIVITIES = """
                INSERT INTO booking_activities (
                    booking_id,
                    activity_id,
                    activity_schedule_id,
                    activity_date,
                    start_time,
                    end_time,
                    number_of_participants,
                    price_per_person,
                    total_price,
                    status,
                    created_at,
                    created_by
                ) VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?
                )
            """;

    public static final String INSERT_BOOKING_INVOICE = """
                INSERT INTO booking_invoices (
                    booking_id,
                    invoice_number,
                    invoice_date,
                    due_date,
                    subtotal,
                    tax_amount,
                    discount_amount,
                    total_amount,
                    amount_paid,
                    balance_due,
                    billing_full_name,
                    billing_address,
                    billing_email,
                    billing_phone,
                    status,
                    created_at,
                    created_by
                ) VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?
                )
            """;


    public static final String GET_BOOKING_BASIC_DETAILS_BY_BOOKING_ID = """
            SELECT
            	b.booking_id,
                b.booking_reference,
            	bi.invoice_number,
                bi.invoice_date,
                bi.due_date,
                bi.subtotal,
                bi.tax_amount,
                bi.discount_amount,
            	bi.insurance_amount,
                p.price_per_person AS package_price,
                bi.total_amount,
                bi.amount_paid,
                bi.balance_due,
                bi.billing_full_name,
                bi.billing_address,
                bi.billing_email,
                bi.billing_phone,
                p.name AS package_name,
                ps.id AS package_schedule_id,
                ps.assume_start_date,
                ps.assume_end_date,
                t.name AS tour_name,
                t.description AS tour_description,
                b.final_amount,
                b.booking_date,
                bs.name AS booking_status
            FROM bookings b
            LEFT JOIN booking_invoices bi
            	ON b.booking_id = bi.booking_id
            LEFT JOIN package_schedule ps
            	ON ps.id = b.package_schedule_id
            LEFT JOIN packages p
            	ON p.package_id =  ps.package_id
            LEFt JOIN tour t
            	ON t.tour_id = p.tour_id
            LEFT JOIN booking_status bs
            	ON bs.id = b.booking_status_id
            WHERE b.booking_id = ?
            """;
    public static final String GET_BOOKING_ACTIVITIES_BY_BOOKING_ID = """
            SELECT
            	ba.booking_id,
                ba.activity_id,
                a.name,
                a.description,
                ba.number_of_participants,
                ba.price_per_person,
                ba.total_price
            FROM booking_activities ba
            LEFT JOIN activities a
            	ON a.id = ba.activity_id
            WHERE ba.booking_id = ?
            """;
    public static final String GET_BOOKING_PARTICIPANTS_BY_BOOKING_ID = """
            SELECT
            	bp.booking_id,
                bp.first_name,
                bp.last_name,
                bp.date_of_birth,
                g.name,
                bp.passport_number,
                c.name,
                bp.email,
                bp.mobile_number,
                bp.medical_conditions,
                bp.allergies
            FROM booking_participants bp
            LEFT JOIN gender g
            	ON g.gender_id = bp.gender_id
            LEFT JOIN country c
            	ON c.country_id = bp.nationality_country_id
            WHERE bp.booking_id = ?
            """;
    public static final String GET_BOOKING_FILTER = """
            SELECT
            	t.tour_id AS tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                p.package_id AS package_id,
                p.name AS package_name,
                p.description AS package_description,
                ps.id AS package_schedule_id,
                ps.name AS package_schedule_name,
                ps.description AS package_schedule_description,
                ps.assume_start_date AS package_schedule_start_date,
                ps.assume_end_date AS package_schedule_end_date
            FROM tour t
            LEFT JOIN packages p
            	ON t.tour_id = p.tour_id
            LEFT JOIN package_schedule ps
            	ON ps.package_id = p.package_id
            """;
    public static final String GET_BOOKED_TOURS_BY_USER_ID = """
            SELECT
            	b.booking_id,
                bi.invoice_number,
                b.booking_reference,
                p.name AS package_name,
                ps.name AS package_schedule_name,
                t.name AS tour_name
            FROM bookings b
            LEFT JOIN booking_invoices bi
            	ON b.booking_id = bi.booking_id
            LEFT JOIN package_schedule ps
            	ON ps.id = b.package_schedule_id
            LEFT JOIN packages p
            	ON p.package_id =  ps.package_id
            LEFt JOIN tour t
            	ON t.tour_id = p.tour_id
            WHERE b.user_id = ?
            """;

    public static final String INSERT_BOOKING_AIRPORT_TRANSPORTATION = """
            INSERT INTO booking_transportation (
                booking_id,
                transport_type,
                departure_date,
                departure_time,
                arrival_date,
                arrival_time,
                departure_location,
                arrival_location,
                created_by,
                created_at
            ) VALUES (
                ?,?, ?, ?, ?, ?, ?, ?, ?, NOW()
            )
            """;

    public static final String GET_PENDING_BOOKING_DETAILS_BY_ID = """
            SELECT
                b.booking_id,
                b.booking_reference,
                b.booking_date,
                bs.name AS booking_status,
                t.tour_id,
                t.name AS tour_name,
                t.description AS tour_description,
                t.duration AS tour_duration,
                t.start_location,
                t.end_location,
                -- tt.name AS tour_type,
                -- tc.name AS tour_category,
                p.package_id,
                p.name AS package_name,
                p.description AS package_description,
                p.total_price AS package_total_price,
                p.discount_percentage,
                p.price_per_person AS package_price_per_person,
                u.username,
                CONCAT(u.first_name, ' ', u.last_name) AS user_full_name,
                u.email,
                u.mobile_number1
            FROM bookings b
            LEFT JOIN booking_status bs
                ON b.booking_status_id = bs.id
            LEFT JOIN packages p
                ON b.package_id = p.package_id
            LEFT JOIN tour t
                ON b.tour_id = t.tour_id
            -- LEFT JOIN tour_type tt
            --     ON t.tour_type = tt.id
            -- LEFT JOIN tour_category tc
            --    ON t.tour_category = tc.id
            LEFT JOIN user u
                ON b.user_id = u.user_id
            WHERE b.user_id = ?
              AND bs.name = 'PENDING_INQUIRY'
            ORDER BY b.travel_end_date DESC
            """;

    public static final String INSERT_TOUR_BOOKING_INQUIRY = """
            INSERT INTO tour_booking_inquiries (
                tour_id,
                package_id,
                booking_status_id,
                user_id,
                name,
                email,
                contact_number,
                country,
                created_by
            ) VALUES (?, ?, (SELECT id FROM booking_status bs WHERE bs.name = ?)  , ?, ?, ?, ?, ?, ?)
            """;

    public static final String INSERT_BOOKING_INQUIRY_TO_BOOKINGS = """
            INSERT INTO bookings (
                booking_reference,
                user_id,
                tour_id,
                package_id,
                booking_status_id,
                created_by,
                booking_date
            )
            VALUES (
                ?,
                ?,
                ?,
                ?,
                (SELECT id FROM booking_status WHERE name = ?),
                ?,
                CURRENT_DATE
            )
            """;

    public static final String CANCELLED_BOOKING_PENDING_REQUEST = """
            UPDATE bookings b
            JOIN booking_status bs
                ON bs.name = ?
            SET
                b.booking_status_id = bs.id,
                b.cancellation_date = NOW(),
                b.updated_by = ?,
                b.updated_at = NOW()
            WHERE b.booking_id = ?
            """;

    public static final String GET_POPULAR_ACTIVITIES = """
    SELECT
        a.id AS activity_id,
        a.name AS activity_name,
        COUNT(DISTINCT ba.booking_id)
            AS total_bookings,
        COALESCE(
            SUM(ba.number_of_participants),
            0
        ) AS total_participants,
        COALESCE(
            SUM(ba.total_price),
            0
        ) AS total_revenue
    FROM booking_activities ba
    INNER JOIN activities a
        ON ba.activity_id = a.id
    GROUP BY
        a.id,
        a.name
    ORDER BY total_bookings DESC
    LIMIT 10
    """;

    public static final String GET_TOP_TOURS = """
    SELECT
        t.tour_id,
        t.name AS tour_name,
        COUNT(*) AS total_bookings,
        COALESCE(SUM(b.total_persons),0)
            AS total_participants,
        COALESCE(SUM(b.final_amount),0)
            AS total_revenue
    FROM bookings b
    INNER JOIN tour t
        ON b.tour_id = t.tour_id
    GROUP BY
        t.tour_id,
        t.name
    ORDER BY total_bookings DESC
    LIMIT 10
    """;

    public static final String GET_BOOKING_FUNNEL = """
    SELECT
        CASE bs.name
            WHEN 'NEW_INQUIRY' THEN 1
            WHEN 'PENDING' THEN 2
            WHEN 'CONTACTED' THEN 3
            WHEN 'QUOTATION_SENT' THEN 4
            WHEN 'NEGOTIATION' THEN 5
            WHEN 'CONFIRMED' THEN 6
            WHEN 'PAYMENT_PENDING' THEN 7
            WHEN 'BOOKED' THEN 8
            WHEN 'COMPLETED' THEN 9
            ELSE 99
        END AS step_order,

        bs.name AS booking_status_name,

        COUNT(*) AS total_bookings,

        ROUND(
            COUNT(*) * 100.0 /
            (
                SELECT COUNT(*)
                FROM bookings b2
                INNER JOIN booking_status bs2
                    ON b2.booking_status_id = bs2.id
                WHERE bs2.name='NEW_INQUIRY'
            ),
            2
        ) AS conversion_percentage

    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id

    GROUP BY
        step_order,
        bs.name

    ORDER BY
        step_order
    """;

    public static final String GET_BOOKING_STATUS_DISTRIBUTION = """
    SELECT
        bs.id AS booking_status_id,
        bs.name AS booking_status_name,
        COUNT(*) AS total_bookings,
        ROUND(
            COUNT(*) * 100.0 /
            (SELECT COUNT(*) FROM bookings),
            2
        ) AS percentage
    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id
    GROUP BY
        bs.id,
        bs.name
    ORDER BY total_bookings DESC
    """;

    public static final String GET_MONTHLY_REVENUE_TRENDS = """
    SELECT
        YEAR(b.created_at) AS year,
        MONTH(b.created_at) AS month,
        COALESCE(SUM(b.final_amount),0) AS total_revenue
    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id
    WHERE bs.name IN ('BOOKED','COMPLETED')
    GROUP BY
        YEAR(b.created_at),
        MONTH(b.created_at)
    ORDER BY
        year,
        month
    """;

    public static final String GET_MONTHLY_BOOKING_TRENDS = """
    SELECT
        YEAR(created_at) AS year,
        MONTH(created_at) AS month,
        COUNT(*) AS total_bookings
    FROM bookings
    GROUP BY
        YEAR(created_at),
        MONTH(created_at)
    ORDER BY
        year,
        month
    """;

    public static final String GET_BOOKING_SUMMARY_STATISTICS = """
    SELECT
        COUNT(*) AS total_bookings,

        COALESCE(
            SUM(
                CASE
                    WHEN bs.name IN ('BOOKED','COMPLETED')
                    THEN b.final_amount
                    ELSE 0
                END
            ),0
        ) AS total_revenue,

        COUNT(
            CASE
                WHEN bs.name IN (
                    'NEW_INQUIRY',
                    'PENDING',
                    'CONTACTED',
                    'QUOTATION_SENT',
                    'NEGOTIATION',
                    'CONFIRMED',
                    'PAYMENT_PENDING',
                    'BOOKED'
                )
                THEN 1
            END
        ) AS active_bookings,

        COUNT(
            CASE
                WHEN bs.name='CANCELLED'
                THEN 1
            END
        ) AS cancelled_bookings,

        COALESCE(SUM(b.total_persons),0) AS total_travellers,

        COALESCE(AVG(b.final_amount),0) AS average_booking_value

    FROM bookings b
    LEFT JOIN booking_status bs
        ON b.booking_status_id = bs.id
    """;

    public static final String GET_BOOKING_STATUS_SUMMARY_STATISTICS = """
    SELECT
        (SELECT COUNT(*) FROM booking_status) AS total_statuses,

        (
            SELECT COUNT(*)
            FROM booking_status bs
            INNER JOIN common_status cs
                ON bs.status = cs.id
            WHERE UPPER(cs.name) = 'ACTIVE'
        ) AS active_statuses,

        (
            SELECT bs.name
            FROM bookings b
            INNER JOIN booking_status bs
                ON b.booking_status_id = bs.id
            GROUP BY bs.id, bs.name
            ORDER BY COUNT(*) DESC
            LIMIT 1
        ) AS most_used_status,

        (
            SELECT COUNT(*)
            FROM bookings b
            INNER JOIN booking_status bs
                ON b.booking_status_id = bs.id
            GROUP BY bs.id
            ORDER BY COUNT(*) DESC
            LIMIT 1
        ) AS most_used_status_count,

        ROUND(
            (
                (
                    SELECT COUNT(*)
                    FROM bookings b
                    INNER JOIN booking_status bs
                        ON b.booking_status_id = bs.id
                    WHERE bs.name IN ('BOOKED','COMPLETED')
                )
                /
                NULLIF(
                    (
                        SELECT COUNT(*)
                        FROM bookings b
                        INNER JOIN booking_status bs
                            ON b.booking_status_id = bs.id
                        WHERE bs.name = 'NEW_INQUIRY'
                    ),
                    0
                )
            ) * 100,
            2
        ) AS inquiry_to_booked_percentage
    """;

    public static final String GET_STATUS_DISTRIBUTIONS = """
    SELECT
        bs.id AS booking_status_id,
        bs.name AS booking_status_name,
        COUNT(*) AS total_bookings,
        ROUND(
            COUNT(*) * 100.0 /
            (SELECT COUNT(*) FROM bookings),
            2
        ) AS percentage
    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id
    GROUP BY
        bs.id,
        bs.name
    ORDER BY total_bookings DESC
    """;

    public static final String GET_STATUS_FUNNELS = """
    SELECT
        CASE bs.name
            WHEN 'NEW_INQUIRY' THEN 1
            WHEN 'PENDING' THEN 2
            WHEN 'CONTACTED' THEN 3
            WHEN 'QUOTATION_SENT' THEN 4
            WHEN 'NEGOTIATION' THEN 5
            WHEN 'CONFIRMED' THEN 6
            WHEN 'PAYMENT_PENDING' THEN 7
            WHEN 'BOOKED' THEN 8
            WHEN 'COMPLETED' THEN 9
            WHEN 'CANCELLED' THEN 10
            WHEN 'REJECTED' THEN 11
            WHEN 'EXPIRED' THEN 12
            ELSE 99
        END AS step_order,

        bs.name AS booking_status_name,

        COUNT(*) AS total_bookings,

        ROUND(
            COUNT(*) * 100.0 /
            NULLIF(
                (
                    SELECT COUNT(*)
                    FROM bookings b2
                    INNER JOIN booking_status bs2
                        ON b2.booking_status_id = bs2.id
                    WHERE bs2.name='NEW_INQUIRY'
                ),
                0
            ),
            2
        ) AS conversion_percentage

    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id
    GROUP BY
        step_order,
        bs.name
    ORDER BY
        step_order
    """;

    public static final String GET_STATUS_TRENDS = """
    SELECT
        YEAR(b.created_at) AS year,
        MONTH(b.created_at) AS month,
        bs.id AS booking_status_id,
        bs.name AS booking_status_name,
        COUNT(*) AS total_bookings
    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id
    GROUP BY
        YEAR(b.created_at),
        MONTH(b.created_at),
        bs.id,
        bs.name
    ORDER BY
        year,
        month,
        booking_status_name
    """;

    public static final String GET_DROP_OFF_STATISTICS = """
    SELECT
        bs.name AS booking_status_name,
        COUNT(*) AS total_bookings,
        ROUND(
            COUNT(*) * 100.0 /
            (SELECT COUNT(*) FROM bookings),
            2
        ) AS percentage
    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id
    WHERE bs.name IN (
        'CANCELLED',
        'REJECTED',
        'EXPIRED'
    )
    GROUP BY
        bs.name
    ORDER BY
        total_bookings DESC
    """;


    public static final String GET_BOOKING_ASSIGN_SUMMARY_STATISTICS = """
    SELECT
        COUNT(*) AS total_bookings,

        COUNT(
            CASE
                WHEN assign_to IS NOT NULL
                THEN 1
            END
        ) AS assigned_bookings,

        COUNT(
            CASE
                WHEN assign_to IS NULL
                THEN 1
            END
        ) AS unassigned_bookings,

        COUNT(DISTINCT assign_to)
            AS total_assigned_employees,

        ROUND(
            COUNT(
                CASE
                    WHEN assign_to IS NOT NULL
                    THEN 1
                END
            ) /
            NULLIF(
                COUNT(DISTINCT assign_to),
                0
            ),
            2
        ) AS average_bookings_per_employee

    FROM bookings
    """;

    public static final String GET_EMPLOYEE_WORKLOADS = """
    SELECT
        e.id AS employee_id,
        u.user_id,
        CONCAT(
            u.first_name,
            ' ',
            u.last_name
        ) AS employee_name,
        ed.designation_name,
        dep.department_name,
        COUNT(b.booking_id)
            AS total_bookings

    FROM bookings b
    INNER JOIN user u
        ON b.assign_to = u.user_id
    INNER JOIN employees e
        ON u.user_id = e.user_id
    LEFT JOIN employee_designations ed
        ON e.designation_id = ed.id
    LEFT JOIN employee_departments dep
        ON e.department_id = dep.id

    GROUP BY
        e.id,
        u.user_id,
        employee_name,
        ed.designation_name,
        dep.department_name

    ORDER BY
        total_bookings DESC
    """;

    public static final String GET_EMPLOYEE_REVENUES = """
    SELECT
        e.id AS employee_id,
        u.user_id,
        CONCAT(
            u.first_name,
            ' ',
            u.last_name
        ) AS employee_name,
        COUNT(b.booking_id)
            AS total_bookings,
        COALESCE(
            SUM(b.final_amount),
            0
        ) AS total_revenue

    FROM bookings b
    INNER JOIN user u
        ON b.assign_to = u.user_id
    INNER JOIN employees e
        ON u.user_id = e.user_id

    GROUP BY
        e.id,
        u.user_id,
        employee_name

    ORDER BY
        total_revenue DESC
    """;

    public static final String GET_DEPARTMENT_DISTRIBUTIONS = """
    SELECT
        dep.id AS department_id,
        dep.department_name,
        COUNT(*) AS total_bookings,

        ROUND(
            COUNT(*) * 100.0 /
            (
                SELECT COUNT(*)
                FROM bookings
                WHERE assign_to IS NOT NULL
            ),
            2
        ) AS percentage

    FROM bookings b
    INNER JOIN employees e
        ON b.assign_to = e.user_id
    INNER JOIN employee_departments dep
        ON e.department_id = dep.id

    GROUP BY
        dep.id,
        dep.department_name

    ORDER BY
        total_bookings DESC
    """;

    public static final String GET_DESIGNATION_DISTRIBUTIONS = """
    SELECT
        ed.id AS designation_id,
        ed.designation_name,
        COUNT(*) AS total_bookings,

        ROUND(
            COUNT(*) * 100.0 /
            (
                SELECT COUNT(*)
                FROM bookings
                WHERE assign_to IS NOT NULL
            ),
            2
        ) AS percentage

    FROM bookings b
    INNER JOIN employees e
        ON b.assign_to = e.user_id
    INNER JOIN employee_designations ed
        ON e.designation_id = ed.id

    GROUP BY
        ed.id,
        ed.designation_name

    ORDER BY
        total_bookings DESC
    """;

    public static final String GET_MONTHLY_ASSIGNMENT_TRENDS = """
    SELECT
        YEAR(created_at) AS year,
        MONTH(created_at) AS month,
        COUNT(*) AS total_assigned_bookings

    FROM bookings
    WHERE assign_to IS NOT NULL

    GROUP BY
        YEAR(created_at),
        MONTH(created_at)

    ORDER BY
        year,
        month
    """;

    public static final String GET_ASSIGNMENT_STATUS_DISTRIBUTIONS = """
    SELECT
        CASE
            WHEN assign_to IS NULL
                THEN 'UNASSIGNED'
            ELSE 'ASSIGNED'
        END AS assignment_type,

        COUNT(*) AS total_bookings,

        ROUND(
            COUNT(*) * 100.0 /
            (SELECT COUNT(*) FROM bookings),
            2
        ) AS percentage

    FROM bookings

    GROUP BY
        assignment_type
    """;

    public static final String GET_BOOKING_HISTORY_SUMMARY_STATISTICS = """
    SELECT
        COUNT(*) AS total_bookings,
        COALESCE(SUM(final_amount), 0) AS total_revenue,
        MIN(booking_date) AS first_booking_date,
        MAX(booking_date) AS latest_booking_date,
        ROUND(
            COUNT(*) /
            NULLIF(
                COUNT(DISTINCT DATE_FORMAT(booking_date, '%Y-%m')),
                0
            ),
            2
        ) AS average_monthly_bookings,
        ROUND(
            COALESCE(SUM(final_amount), 0) /
            NULLIF(
                COUNT(DISTINCT DATE_FORMAT(booking_date, '%Y-%m')),
                0
            ),
            2
        ) AS average_monthly_revenue
    FROM bookings
    """;


    public static final String GET_BOOKING_GROWTH_TRENDS = """
    SELECT
        YEAR(booking_date) AS year,
        MONTH(booking_date) AS month,
        COUNT(*) AS total_bookings
    FROM bookings
    WHERE booking_date IS NOT NULL
    GROUP BY
        YEAR(booking_date),
        MONTH(booking_date)
    ORDER BY
        year,
        month
    """;


    public static final String GET_REVENUE_GROWTH_TRENDS = """
    SELECT
        YEAR(booking_date) AS year,
        MONTH(booking_date) AS month,
        COALESCE(SUM(final_amount), 0) AS total_revenue
    FROM bookings
    WHERE booking_date IS NOT NULL
    GROUP BY
        YEAR(booking_date),
        MONTH(booking_date)
    ORDER BY
        year,
        month
    """;


    public static final String GET_BOOKING_STATUS_HISTORIES = """
    SELECT
        YEAR(b.booking_date) AS year,
        MONTH(b.booking_date) AS month,
        bs.id AS booking_status_id,
        bs.name AS booking_status_name,
        COUNT(*) AS total_bookings
    FROM bookings b
    INNER JOIN booking_status bs
        ON b.booking_status_id = bs.id
    WHERE b.booking_date IS NOT NULL
    GROUP BY
        YEAR(b.booking_date),
        MONTH(b.booking_date),
        bs.id,
        bs.name
    ORDER BY
        year,
        month,
        booking_status_name
    """;


    public static final String GET_CANCELLATION_TRENDS = """
    SELECT
        YEAR(cancellation_date) AS year,
        MONTH(cancellation_date) AS month,
        COUNT(*) AS total_cancelled_bookings,
        ROUND(
            COUNT(*) * 100.0 /
            NULLIF(
                (
                    SELECT COUNT(*)
                ),
                0
            ),
            2
        ) AS cancellation_rate
    FROM bookings
    WHERE cancellation_date IS NOT NULL
    GROUP BY
        YEAR(cancellation_date),
        MONTH(cancellation_date)
    ORDER BY
        year,
        month
    """;


    public static final String GET_HISTORICAL_TOP_TOURS = """
    SELECT
        t.tour_id,
        t.name AS tour_name,
        COUNT(b.booking_id) AS total_bookings,
        COALESCE(SUM(b.total_persons), 0) AS total_participants,
        COALESCE(SUM(b.final_amount), 0) AS total_revenue
    FROM bookings b
    INNER JOIN tour t
        ON b.tour_id = t.tour_id
    GROUP BY
        t.tour_id,
        t.name
    ORDER BY
        total_bookings DESC,
        total_revenue DESC
    """;


    public static final String GET_CUSTOMER_RETURN_STATISTICS = """
    SELECT
        customer_type,
        COUNT(*) AS total_customers,
        ROUND(
            COUNT(*) * 100.0 /
            (
                SELECT COUNT(DISTINCT user_id)
                FROM bookings
                WHERE user_id IS NOT NULL
            ),
            2
        ) AS percentage
    FROM
    (
        SELECT
            user_id,
            CASE
                WHEN COUNT(*) = 1
                    THEN 'NEW_CUSTOMER'
                ELSE 'RETURNING_CUSTOMER'
            END AS customer_type
        FROM bookings
        WHERE user_id IS NOT NULL
        GROUP BY user_id
    ) x
    GROUP BY customer_type
    """;


    public static final String GET_PEAK_BOOKING_PERIODS = """
    SELECT
        MONTH(booking_date) AS month,
        MONTHNAME(booking_date) AS month_name,
        COUNT(*) AS total_bookings
    FROM bookings
    WHERE booking_date IS NOT NULL
    GROUP BY
        MONTH(booking_date),
        MONTHNAME(booking_date)
    ORDER BY
        total_bookings DESC
    """;

    public static final String GET_BOOKING_BASIC_DETAILS_FOR_PARAMS = """
        SELECT
            b.booking_id,
            b.booking_reference,
            b.booking_date,
            b.travel_start_date,
            b.travel_end_date,
            b.total_persons,
            b.total_amount,
            b.discount_amount,
            b.tax_amount,
            b.insurance_amount,
            b.final_amount,
            b.insurance_required,
            b.assign_to,
            b.assign_message,
            DATE(b.cancellation_date) AS cancellation_date,
            b.refund_amount,
            b.special_requirements,
            b.dietary_restrictions,

            u.user_id,
            u.username,
            CONCAT(
                COALESCE(u.first_name, ''),
                ' ',
                COALESCE(u.last_name, '')
            ) AS customer_name,
            u.email,
            u.mobile_number1,

            t.tour_id,
            t.name AS tour_name,
            t.duration AS tour_duration,
            t.start_location,
            t.end_location,

            p.package_id,
            p.name AS package_name,

            bs.id AS booking_status_id,
            bs.name AS booking_status_name,

            au.user_id AS assigned_employee_id,
            CONCAT(
                COALESCE(au.first_name, ''),
                ' ',
                COALESCE(au.last_name, '')
            ) AS assigned_employee_name

        FROM bookings b
        LEFT JOIN user u
            ON b.user_id = u.user_id
        LEFT JOIN tour t
            ON b.tour_id = t.tour_id
        LEFT JOIN packages p
            ON b.package_id = p.package_id
        LEFT JOIN booking_status bs
            ON b.booking_status_id = bs.id
        LEFT JOIN employees e
            ON b.assign_to = e.id
        LEFT JOIN user au
            ON e.user_id = au.user_id
        WHERE 1=1
        """;

    public static final String GET_BOOKING_COUNT_FOR_PARAMS = """
        SELECT COUNT(*)
        FROM bookings b
        LEFT JOIN user u
            ON b.user_id = u.user_id
        LEFT JOIN tour t
            ON b.tour_id = t.tour_id
        LEFT JOIN packages p
            ON b.package_id = p.package_id
        WHERE 1=1
        """;
    public static final String GET_BOOKINGS_REQUEST_PARAMS = """
        SELECT
            COALESCE(MIN(final_amount),0) AS min_price,
            COALESCE(MAX(final_amount),0) AS max_price,
            COALESCE(MIN(discount_amount),0) AS min_discount_amount,
            COALESCE(MAX(discount_amount),0) AS max_discount_amount,
            MIN(booking_date) AS min_booking_date,
            MAX(booking_date) AS max_booking_date,
            MIN(travel_start_date) AS min_travel_start_date,
            MAX(travel_start_date) AS max_travel_start_date
        FROM bookings
        """;

    public static final String GET_BOOKING_PARAM_STATUSES = """
        SELECT
            id,
            name
        FROM booking_status
        ORDER BY name
        """;

    public static final String GET_BOOKING_PARAM_TOURS = """
        SELECT
            tour_id AS id,
            name
        FROM tour
        ORDER BY name
        """;
    public static final String GET_BOOKING_PARAM_PACKAGES = """
        SELECT
            package_id AS id,
            name
        FROM packages
        ORDER BY name
        """;
    public static final String GET_BOOKING_PARAM_ASSIGN_EMPLOYEES = """
        SELECT
            e.id,
            CONCAT(
                COALESCE(u.first_name,''),
                ' ',
                COALESCE(u.last_name,'')
            ) AS name
        FROM employees e
        INNER JOIN user u
            ON e.user_id = u.user_id
        ORDER BY name
        """;

    public static final String GET_BOOKING_INFORMATION_BY_ID = """
        SELECT
            b.booking_id,
            b.booking_reference,
            b.booking_date,
            b.travel_start_date,
            b.travel_end_date,
            b.total_persons,
            b.total_amount,
            b.discount_amount,
            b.tax_amount,
            b.insurance_amount,
            b.final_amount,
            b.insurance_required,
            b.special_requirements,
            b.dietary_restrictions
        FROM bookings b
        WHERE b.booking_id = ?
        """;


    public static final String GET_CUSTOMER_INFORMATION_BY_BOOKING_ID = """
        SELECT
            u.user_id,
            u.username,
            u.first_name,
            u.last_name,
            CONCAT(
                COALESCE(u.first_name,''),
                ' ',
                COALESCE(u.last_name,'')
            ) AS full_name,
            u.email,
            u.mobile_number1,
            u.passport_number
        FROM bookings b
        INNER JOIN user u
            ON b.user_id = u.user_id
        WHERE b.booking_id = ?
        """;


    public static final String GET_TOUR_INFORMATION_BY_BOOKING_ID = """
        SELECT
            t.tour_id,
            t.name AS tour_name,
            t.description AS tour_description,
            t.duration,
            t.start_location,
            t.end_location,
            t.latitude,
            t.longitude
        FROM bookings b
        INNER JOIN tour t
            ON b.tour_id = t.tour_id
        WHERE b.booking_id = ?
        """;


    public static final String GET_PACKAGE_INFORMATION_BY_BOOKING_ID = """
        SELECT
            p.package_id,
            p.name AS package_name,
            p.description AS package_description,
            p.total_price,
            p.price_per_person,
            p.discount_percentage
        FROM bookings b
        INNER JOIN packages p
            ON b.package_id = p.package_id
        WHERE b.booking_id = ?
        """;


    public static final String GET_BOOKING_STATUS_INFORMATION_BY_BOOKING_ID = """
        SELECT
            bs.id,
            bs.name,
            bs.description
        FROM bookings b
        INNER JOIN booking_status bs
            ON b.booking_status_id = bs.id
        WHERE b.booking_id = ?
        """;


    public static final String GET_ASSIGNMENT_INFORMATION_BY_BOOKING_ID = """
        SELECT
            e.id AS employee_id,
            e.user_id,
            e.employee_code,
            CONCAT(
                COALESCE(u.first_name,''),
                ' ',
                COALESCE(u.last_name,'')
            ) AS employee_name,
            ed.department_name,
            des.designation_name,
            b.assign_message
        FROM bookings b
        INNER JOIN employees e
            ON b.assign_to = e.id
        INNER JOIN user u
            ON e.user_id = u.user_id
        LEFT JOIN employee_departments ed
            ON e.department_id = ed.id
        LEFT JOIN employee_designations des
            ON e.designation_id = des.id
        WHERE b.booking_id = ?
        """;


    public static final String GET_PARTICIPANTS_BY_BOOKING_ID = """
        SELECT
            bp.id,
            bp.first_name,
            bp.last_name,
            CONCAT(
                COALESCE(bp.first_name,''),
                ' ',
                COALESCE(bp.last_name,'')
            ) AS full_name,
            bp.date_of_birth,
            g.name AS gender_name,
            c.name AS country_name,
            bp.passport_number,
            bp.email,
            bp.mobile_number,
            bp.emergency_contact_name,
            bp.emergency_contact_phone,
            bp.emergency_contact_relationship,
            bp.medical_conditions,
            bp.allergies,
            bp.special_assistance_required,
            bp.assistance_details
        FROM booking_participants bp
        LEFT JOIN gender g
            ON bp.gender_id = g.gender_id
        LEFT JOIN country c
            ON bp.nationality_country_id = c.country_id
        WHERE bp.booking_id = ?
        ORDER BY bp.id
        """;


    public static final String GET_CANCELLATION_INFORMATION_BY_BOOKING_ID = """
        SELECT
            DATE(b.cancellation_date) AS cancellation_date,
            cr.name,
            b.cancellation_notes,
            b.refund_amount,
            rs.name AS refund_status
        FROM bookings b
        LEFT JOIN cancellation_reasons cr
            ON b.cancellation_reason_id = cr.id
        LEFT JOIN refund_status rs
            ON b.refund_status_id = rs.id
        WHERE b.booking_id = ?
        """;


    public static final String GET_ACCOMMODATIONS_BY_BOOKING_ID = """
        SELECT
            ba.id,
            sp.name AS hotel_name,
            ba.room_type,
            ba.room_number,
            ba.confirmation_number,
            ba.check_in_date,
            ba.check_out_date
        FROM booking_accommodation ba
        LEFT JOIN service_provider sp
        ON sp.service_provider_id = ba.hotel_id
        WHERE ba.booking_id = ?
        ORDER BY ba.check_in_date
        """;


    public static final String GET_TRANSPORTATIONS_BY_BOOKING_ID = """
        SELECT
            bt.id,
            bt.transport_type,
            bt.departure_date,
            bt.departure_time,
            bt.arrival_date,
            bt.arrival_time,
            bt.departure_location,
            bt.arrival_location,
            bt.carrier_name,
            bt.reference_number,
            bt.seat_numbers,
            v.registration_number
        FROM booking_transportation bt
        LEFT JOIN vehicles v
        ON v.vehicle_id = bt.vehicle_id
        WHERE bt.booking_id = ?
        ORDER BY bt.departure_date,
                 bt.departure_time
        """;


    public static final String GET_ACTIVITIES_BY_BOOKING_ID = """
        SELECT
            ba.id,
            a.id AS activity_id,
            a.name AS activity_name,
            ba.activity_date,
            ba.start_time,
            ba.end_time,
            ba.number_of_participants,
            ba.price_per_person,
            ba.total_price,
            cs.name AS status_name
        FROM booking_activities ba
        INNER JOIN activities a
            ON ba.activity_id = a.id
        LEFT JOIN common_status cs
            ON ba.status = cs.id
        WHERE ba.booking_id = ?
        ORDER BY ba.activity_date,
                 ba.start_time
        """;

    public static final String INSERT_BOOKING = """
    INSERT INTO bookings (
        booking_reference,
        user_id,
        package_schedule_id,
        total_persons,
        total_amount,
        discount_amount,
        tax_amount,
        insurance_amount,
        final_amount,
        booking_date,
        travel_start_date,
        travel_end_date,
        booking_status_id,
        special_requirements,
        dietary_restrictions,
        insurance_required,
        created_by,
        tour_id,
        package_id,
        assign_to,
        assign_message
    )
    VALUES (
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
        ?, ?, ?, ?, ?, ?, ?, ?, ?
    )
    """;

    public static final String ADD_BOOKING_PARTICIPANT = """
    INSERT INTO booking_participants (
        booking_id,
        first_name,
        last_name,
        date_of_birth,
        gender_id,
        passport_number,
        nationality_country_id,
        email,
        mobile_number,
        emergency_contact_name,
        emergency_contact_phone,
        emergency_contact_relationship,
        medical_conditions,
        allergies,
        special_assistance_required,
        assistance_details,
        room_sharing_with,
        created_by
    )
    VALUES (
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
    )
    """;

    public static final String ADD_BOOKING_ACCOMMODATION = """
    INSERT INTO booking_accommodation (
        booking_id,
        check_in_date,
        check_out_date,
        hotel_id,
        room_type,
        room_number,
        confirmation_number,
        created_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String ADD_BOOKING_TRANSPORTATION = """
    INSERT INTO booking_transportation (
        booking_id,
        transport_type,
        departure_date,
        departure_time,
        arrival_date,
        arrival_time,
        departure_location,
        arrival_location,
        carrier_name,
        reference_number,
        seat_numbers,
        created_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String ADD_BOOKING_ACTIVITY = """
    INSERT INTO booking_activities (
        booking_id,
        activity_id,
        activity_schedule_id,
        activity_date,
        start_time,
        end_time,
        number_of_participants,
        price_per_person,
        total_price,
        status,
        created_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String ADD_BOOKING_DOCUMENT = """
    INSERT INTO booking_documents (
        booking_id,
        document_type,
        document_name,
        document_url,
        file_size,
        mime_type,
        status,
        created_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String ADD_BOOKING_INSURANCE = """
    INSERT INTO booking_insurance (
        booking_id,
        insurance_provider,
        policy_number,
        coverage_type,
        premium_amount,
        coverage_details,
        policy_start_date,
        policy_end_date,
        created_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String ADD_BOOKING_NOTE = """
    INSERT INTO booking_notes (
        booking_id,
        note_type,
        note_text,
        is_important,
        follow_up_date,
        follow_up_completed,
        created_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String ADD_BOOKING_PRICE_BREAKDOWN = """
    INSERT INTO booking_price_breakdown (
        booking_id,
        item_type,
        item_name,
        item_description,
        quantity,
        unit_price,
        total_price,
        created_by
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    public static final String ADD_BOOKING_INVOICE = """
    INSERT INTO booking_invoices (
        booking_id,
        invoice_number,
        invoice_date,
        due_date,
        subtotal,
        tax_amount,
        discount_amount,
        total_amount,
        amount_paid,
        balance_due,
        billing_full_name,
        billing_address,
        billing_email,
        billing_phone,
        status,
        insurance_amount,
        created_by
    )
    VALUES (
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
    )
    """;

}


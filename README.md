# CardMaster
TỔNG HỢP YÊU CẦU VÀ ĐẶC TẢ CHỨC NĂNG ỨNG DỤNG ANDROID
Đồ án môn Lập trình di động — NLU
Người thực hiện trao đổi: Hoàng Vũ Lê Huy và nhóm đồ án
Ngày tổng hợp: 23/11/2026
Tài liệu tổng hợp các nội dung đã thảo luận và các quyết định đã chốt của nhóm. Không bao gồm nội dung DESIGN.md.
1. Tổng quan đề tài
CardMaster là ứng dụng Android hỗ trợ người dùng quản lý tài liệu ôn tập theo môn học/chủ đề, học bằng thẻ lật, luyện trắc nghiệm có giải thích, tạo nội dung từ tài liệu bằng Gemini AI và theo dõi kết quả học tập.
•	Tên ứng dụng chính thức: CardMaster.
•	Tên làm việc trước đây: FlashQuiz.
•	Người dùng chính: người học tự tạo, nhập và quản lý nội dung ôn tập cá nhân. Chưa đặt yêu cầu riêng về quản trị viên.
•	Hai loại nội dung: thẻ ôn tập (Flashcard) và thẻ trắc nghiệm (Quiz Card).
•	Không sử dụng Onboarding. Import file được đặt trong Storage và có thể truy cập từ chi tiết bộ thẻ.
•	Flashcard và Quiz Card có thể được lưu trong cùng một danh mục/bộ thẻ.
2. Cấu hình dự án
Hạng mục	Cấu hình
Môi trường phát triển	Android Studio
Nền tảng hỗ trợ tối thiểu	Android 7.0
Minimum SDK	API 24 (minSdk = 24)
Ngôn ngữ lập trình ứng dụng	Java
Build script	Gradle Kotlin DSL — build.gradle.kts
compileSdk / targetSdk	Chưa chốt trong trao đổi; không mặc định đặt bằng 24.
Giao diện	Đề xuất XML Layout + Activity/Fragment để phù hợp với lựa chọn Java.
Công cụ quản lý mã nguồn	Đề xuất Git/GitHub.
Lưu ý: “Kotlin DLS” được chuẩn hóa thành “Kotlin DSL”. Kotlin DSL chỉ là ngôn ngữ của file cấu hình Gradle; phần mã ứng dụng vẫn viết bằng Java. Các ví dụ Kotlin/Jetpack Compose trong trao đổi trước không còn là cấu hình triển khai mặc định.
3. Nhóm chức năng
Nhóm	Phạm vi
Tài khoản	Đăng ký, đăng nhập, đăng xuất; quên/đổi mật khẩu; quản lý hồ sơ.
Quản lý nội dung	Tạo, sửa, xóa môn/chủ đề, bộ thẻ và hai loại thẻ; tìm kiếm và lọc.
Ôn tập	Lật thẻ; đánh dấu đã nhớ/chưa nhớ; lưu kết quả phiên học.
Trắc nghiệm	Chọn đáp án, ghi nhận đúng/sai/bỏ qua, lật thẻ giải thích, chấm điểm và lưu lịch sử.
Import/Export	Nhập CSV/Excel vào cơ sở dữ liệu; xuất CSV/Excel.
Gemini AI	Tạo flashcard, tạo trắc nghiệm hoặc trích câu hỏi có sẵn từ PDF/Word; chat hỏi đáp.
Theo dõi học tập	Lịch sử, tiến độ, thống kê thời gian học và bài tập đã làm.
Lịch và thông báo	Lập lịch ôn, nhắc học, đồng bộ Google Calendar hai chiều, quản lý thông báo.
4. Tài khoản và hồ sơ
•	Đăng ký: họ tên, email, mật khẩu và xác nhận mật khẩu; kiểm tra dữ liệu nhập.
•	Đăng nhập: email, mật khẩu; hỗ trợ hiện/ẩn mật khẩu và thông báo lỗi.
•	Quên mật khẩu: nhập email/Gmail để nhận liên kết đặt lại mật khẩu; không gửi lại mật khẩu cũ.
•	Hồ sơ: ảnh đại diện, tên, email và các số liệu học tập cơ bản.
•	Chỉnh sửa hồ sơ, đổi mật khẩu, đăng xuất; xác nhận trước khi thực hiện thao tác quan trọng.
•	Đăng nhập Google là tùy chọn mở rộng, chưa phải yêu cầu bắt buộc đã chốt.
5. Tổ chức kho nội dung
Cấu trúc cơ bản: Người dùng → Môn học/Chủ đề → Bộ thẻ → Thẻ. Người dùng tự nhập và tạo danh mục mới; một bộ có thể chứa cả Flashcard và Quiz Card.
•	Storage là nơi quản lý môn học, bộ thẻ, thẻ, import/export và tạo nội dung bằng AI.
•	Cho phép tạo/sửa/xóa, tìm kiếm, lọc theo loại thẻ và trạng thái học.
•	Chi tiết bộ thẻ hiển thị tên, môn học, tổng số thẻ, số thẻ từng loại, tiến độ và lần học gần nhất.
•	Hành động chính tại bộ thẻ: Học Flashcard, Làm Quiz, Thêm thẻ, Nhập file, Tạo bằng AI, Xuất file, Lập lịch ôn.
•	“Học tất cả” xen kẽ hai loại thẻ chỉ là ý tưởng mở rộng, chưa cần đưa vào luồng bắt buộc.
6. Hai loại thẻ và cơ chế học
6.1. Flashcard ôn tập
Mặt trước là câu hỏi/thuật ngữ; mặt sau là thông tin hoặc đáp án. Người dùng lật để đọc, sau đó chọn “Đã nhớ” hoặc “Chưa nhớ”. Cuối phiên hiển thị số thẻ học, số đã nhớ/chưa nhớ và thời gian học.
6.2. Quiz Card
•	Mỗi thẻ gồm câu hỏi, bốn lựa chọn, một đáp án đúng và phần giải thích.
•	Trước khi trả lời chỉ hiển thị câu hỏi và lựa chọn; chưa mở phần giải thích.
•	Sau khi chọn: ghi nhận đúng/sai, hiển thị thẻ kết quả có thể lật để đọc giải thích.
•	Nếu chọn sai, vẫn phải cho lật thẻ, hiển thị đáp án đúng và giải thích đáp án đúng.
•	Nếu có dữ liệu nguồn, hiển thị tên tài liệu, trang hoặc đoạn trích để kiểm chứng.
•	Đề xuất khóa đáp án sau khi đã trả lời để không làm thay đổi kết quả của câu đó.
•	Nút “Câu tiếp theo” chuyển câu; câu cuối chuyển tới hoàn thành và kết quả.
Luồng Quiz: Chọn bộ → thiết lập bài → trả lời → xem kết quả câu → lật thẻ xem giải thích → câu tiếp → kết quả tổng. Cơ chế này ưu tiên luyện tập có phản hồi ngay, không phải thi giấu đáp án đến cuối.
Thiết lập bài dự kiến: số câu 5/10/20/toàn bộ; giới hạn thời gian là tùy chọn.
7. Import và Export CSV/Excel
Import nằm trong Storage hoặc chi tiết bộ thẻ, không phải một mục riêng trên thanh điều hướng dưới. Có thể dùng màn hình con hoặc bottom sheet để chọn file, sau đó mở phần xem trước.
•	Luồng nhập: chọn CSV/Excel → chọn bộ đích và loại nội dung → đọc/kiểm tra → xem trước → xác nhận → lưu database.
•	Xem trước cần có số dòng hợp lệ, lỗi, trùng lặp và khả năng bỏ chọn dòng.
•	Flashcard cần mặt trước và mặt sau. Quiz Card cần câu hỏi, bốn lựa chọn, đáp án đúng và giải thích.
•	Cho phép nhận diện loại dựa trên cột type hoặc cấu trúc dữ liệu.
•	Luồng xuất: chọn nội dung → chọn CSV/Excel → chọn nơi lưu → thông báo kết quả.
Loại	Cột dữ liệu gợi ý
Flashcard	type, front, back, subject, deck
Quiz Card	type, question, option_a, option_b, option_c, option_d, correct_index, explanation, subject, deck
Nguồn tùy chọn	source_file, source_page, source_excerpt
Quy ước đề xuất: correct_index = 0, 1, 2, 3 tương ứng A, B, C, D. Mẫu CSV/Excel phải dùng đúng số cột và được kiểm thử trước khi demo.
8. Tạo nội dung bằng Gemini AI
8.1. Ba chế độ
Chế độ	Yêu cầu
Tạo Flashcard	Tạo cặp câu hỏi–đáp án từ kiến thức trong tài liệu.
Tạo trắc nghiệm	Tạo câu hỏi, bốn lựa chọn, đáp án đúng và giải thích dựa trên tài liệu.
Trích câu hỏi có sẵn	Giữ nội dung câu hỏi/lựa chọn đã có; chuẩn hóa để lưu. Không đánh đồng với tự sinh câu hỏi mới.
8.2. Quy trình
Chọn PDF/Word → chọn chế độ, bộ đích và số lượng → gửi xử lý Gemini → nhận dữ liệu có cấu trúc → kiểm tra → xem trước/chỉnh sửa → người dùng xác nhận → lưu database.
•	Tách biệt rõ nội dung trích từ tài liệu và nội dung do AI suy luận/sinh mới.
•	Trong chế độ trích xuất, nếu tài liệu thiếu đáp án thì đánh dấu cần kiểm tra; không âm thầm coi đáp án AI suy luận là đáp án gốc.
•	Kết quả Quiz Card phải có explanation để phục vụ lật thẻ giải thích.
•	Kiểm tra nội dung trống, số lựa chọn, chỉ số đáp án đúng và trùng lặp trước khi lưu.
•	Kết quả AI là bản nháp; không lưu tự động nếu chưa được người dùng duyệt.
•	Thể hiện trạng thái đang đọc, đang tạo nội dung, thành công, lỗi và thử lại.
8.3. Trường đầu ra dự kiến
Loại	Trường
Flashcard	front, back, topic (tùy chọn), difficulty (tùy chọn)
Quiz Card	question, options[4], correctIndex, explanation
Thông tin nguồn	sourceFile, sourcePage, sourceExcerpt (nếu xác định được)
8.4. Chat với AI
Tab Chat AI dùng Gemini để trả lời câu hỏi học tập. Giao diện gồm lịch sử tin nhắn, ô nhập, nút gửi, trạng thái đang trả lời và lỗi/thử lại. Đính kèm tài liệu trong chat hoặc chuyển câu trả lời thành thẻ là tùy chọn mở rộng; không thay thế luồng tạo nội dung từ file trong Storage.
9. Lịch sử, thống kê và tiến độ
9.1. Lịch sử
Lưu phiên học Flashcard và lần làm Quiz, gồm thời gian bắt đầu/kết thúc, bộ thẻ, số lượng thẻ/câu, kết quả và thời lượng. Home hiển thị tiến độ hoạt động gần đây và nút học tiếp.
9.2. Thời gian học tập
•	Hiển thị tổng thời gian học, thời gian trung bình mỗi phiên và số phiên.
•	Bộ lọc dự kiến: Hôm nay, 7 ngày, 30 ngày, Tất cả.
•	Biểu đồ cột thời gian theo ngày là đề xuất bổ sung để đọc xu hướng.
•	Đề xuất chỉ tính thời gian học thực tế trong Flashcard/Quiz; tạm dừng bộ đếm khi rời phiên hoặc đưa app ra nền.
9.3. Bài tập đã làm
•	Ưu tiên biểu đồ hình tròn dạng donut theo yêu cầu người dùng.
•	Đề xuất donut chính thể hiện số câu đúng/sai; nếu có bỏ qua, thêm nhóm chưa trả lời trong các bài được đưa vào thống kê.
•	Ở giữa donut hiển thị tỉ lệ đúng, bên cạnh có số liệu và chú thích.
•	Hiển thị bổ sung: số bài Quiz đã hoàn thành, tổng số câu đã làm, điểm trung bình và lịch sử từng bài.
•	Không trộn số bài và số câu trong cùng một biểu đồ; nhãn phải ghi rõ đơn vị.
•	Không có dữ liệu: hiển thị trạng thái trống, không hiển thị tỉ lệ 0% như kết quả học thực tế.
10. Lịch ôn tập, Calendar và thông báo
•	Tạo lịch theo bộ thẻ: ngày giờ, lặp lại một lần/hàng ngày/hàng tuần và bật/tắt nhắc.
•	Google Calendar được đồng bộ hai chiều: tạo, cập nhật và xóa sự kiện ôn tập.
•	Khi người dùng thay đổi lịch trong CardMaster, ứng dụng cập nhật sự kiện tương ứng trên Google Calendar.
•	Khi người dùng thay đổi/xóa sự kiện tương ứng trên Google Calendar, ứng dụng cập nhật hoặc xóa lịch ôn tập trong CardMaster.
•	Cần lưu calendarEventId để ánh xạ lịch trong app với sự kiện trên Google Calendar.
•	Cần xử lý xung đột khi cả app và Google Calendar thay đổi cùng một sự kiện.
•	Khi mất kết nối, lưu thay đổi cục bộ và đồng bộ lại khi có mạng.
•	Thông báo gồm nhắc ôn, hoàn thành tác vụ nhập/AI và các thông tin học tập phù hợp.
•	Danh sách thông báo có đọc/chưa đọc, xóa từng mục và xóa tất cả.
•	Sau khi xóa hết, cùng màn hình thông báo chuyển sang trạng thái trống; không cần một màn hình độc lập.
11. Điều hướng và danh mục màn hình
Luồng khởi động: Splash → Login nếu chưa đăng nhập; Splash → Home nếu đã đăng nhập. Không có Onboarding.
Thanh điều hướng dưới gồm đúng 5 mục: Home — Thống kê — Chat AI — Storage — Profile. Các màn hình con ẩn thanh dưới và có nút quay lại phù hợp.
Nhóm	Màn hình/biến thể	Nội dung chính
Khởi động	Splash	Logo, tên app; kiểm tra phiên đăng nhập.
Xác thực	Login / Register / Forgot Password	Đăng nhập, đăng ký và gửi liên kết đặt lại mật khẩu.
Tab Home	Home	Tổng số thẻ; tiến độ gần đây; học tiếp; chuông thông báo góc trên phải.
Thông báo	Notifications	Danh sách, đọc/xóa; trạng thái trống sau khi xóa hết.
Tab Thống kê	Statistics	Thời gian học, donut kết quả bài tập, lịch sử.
Tab Chat AI	AI Chat	Hỏi đáp Gemini; nhập/gửi và hiển thị hội thoại.
Tab Storage	Storage	Môn/chủ đề, bộ thẻ, tìm kiếm/lọc; điểm vào import/export và AI.
Danh mục	Subject List / Subject Editor	Xem, tạo, sửa và xóa môn/chủ đề; list có thể tích hợp Storage.
Bộ thẻ	Deck List / Deck Editor / Deck Detail	Quản lý bộ và truy cập các hoạt động học.
Thẻ	Card List / Card Editor	Hai loại thẻ; editor đổi trường nhập theo loại; list có thể tích hợp Deck Detail.
Học thẻ	Flashcard Study / Study Result	Lật, đánh dấu đã nhớ/chưa nhớ, kết quả phiên học.
Quiz	Quiz Setup / Quiz Play / Quiz Result	Thiết lập, trả lời, lật giải thích và xem kết quả tổng.
Nhập file	Import File / Import Preview	Màn hình con thuộc Storage; chọn CSV/Excel, xem trước và lưu.
Xuất file	Export File	Chọn CSV/Excel và nơi lưu; có thể dùng dialog/bottom sheet.
AI tài liệu	AI Document Import / AI Preview	PDF/Word; chế độ xử lý; duyệt và sửa kết quả.
Lịch	Study Planner	Danh sách/tạo/sửa lịch ôn và thao tác Calendar.
Kết quả Calendar	Calendar Sync Result	Có thể dùng dialog/snackbar thay màn hình riêng.
Tab Profile	Profile	Ảnh, tên, email, thông tin học tập và menu quản lý.
Hồ sơ	Edit Profile / Change Password	Sửa thông tin và đổi mật khẩu.
Cài đặt	Settings	Thông báo, đăng xuất; theme/ngôn ngữ là tùy chọn.
Tìm kiếm	Search	Có thể tích hợp ngay Storage và danh sách thẻ.
12. Chi tiết các màn hình trọng tâm
12.1. Home
Phía trên: lời chào và chuông thông báo bên phải. Nội dung: tổng số thẻ hiện có, số đã nhớ/cần ôn, lịch sử hoặc tiến độ gần đây, nút học tiếp và lịch ôn sắp tới. Phía dưới là 5 tab chính.
12.2. Storage và chi tiết bộ thẻ
Storage có tìm kiếm, bộ lọc môn/chủ đề, danh sách bộ thẻ và nút thêm. Import file được đặt rõ tại đây. Chi tiết bộ thẻ có các nút học Flashcard, làm Quiz, thêm/sửa thẻ, nhập/xuất, tạo bằng AI và lập lịch.
12.3. Trình sửa thẻ
Chọn loại Flashcard hoặc Quiz Card. Flashcard nhập mặt trước/mặt sau. Quiz Card nhập câu hỏi, A–D, đáp án đúng và giải thích. Có Lưu và tùy chọn Lưu & tạo tiếp. Dữ liệu nguồn là tùy chọn.
12.4. Trạng thái cần có
Các màn hình danh sách cần trạng thái đang tải, có dữ liệu, rỗng và lỗi/thử lại. Các thao tác AI/import cần báo tiến trình và cho biết dữ liệu chỉ được lưu sau khi xác nhận. Các thao tác xóa cần xác nhận phù hợp.
12.5. Quy tắc quay lại
•	Tab gốc không cần nút Back trên thanh tiêu đề; màn hình con có nút Back.
•	Khi thoát phiên học hoặc bài Quiz đang làm, hỏi xác nhận để tránh mất tiến độ.
•	Khi thoát biểu mẫu có thay đổi chưa lưu, hỏi lưu hoặc bỏ thay đổi.
•	Quiz không bị khóa đường thoát; nút Back hệ thống phải được xử lý nhất quán.
13. Kiến trúc và dữ liệu đề xuất
Điều chỉnh theo Java: XML Layout + Activity/Fragment → ViewModel → Repository → nguồn dữ liệu. Room là lựa chọn đề xuất cho dữ liệu cục bộ; Firebase Authentication và Firestore là các phương án đã thảo luận cho tài khoản và dữ liệu đám mây, chưa phải lựa chọn cuối cùng.
Thực thể	Thông tin chính
User/Profile	Định danh người dùng, tên, email, ảnh.
Subject	Chủ sở hữu, tên môn/chủ đề, mô tả.
Deck	Chủ sở hữu, môn/chủ đề, tiêu đề, mô tả, thời gian tạo/cập nhật.
Flashcard	Bộ thẻ, mặt trước, mặt sau, trạng thái nhớ.
QuizQuestion / QuizOption	Bộ thẻ, câu hỏi, lựa chọn, đáp án đúng, giải thích, nguồn.
StudySession	Bộ thẻ, thời gian, số thẻ học, đã nhớ/chưa nhớ.
QuizAttempt / AttemptAnswer	Bài làm, câu đã trả lời, lựa chọn đã chọn, kết quả, thời lượng, điểm.
StudyPlan	Bộ thẻ, lịch, lặp lại, trạng thái và mã sự kiện Calendar.
Notification	Người nhận, nội dung, thời gian, trạng thái đã đọc.
Cách lưu hai loại thẻ còn cần chốt: một bảng Cards có trường type hoặc tách Flashcard và QuizQuestion. Không triển khai đồng thời hai mô hình mà không có quy tắc rõ ràng. Kết quả mỗi lần làm phải lưu riêng để giữ lịch sử; không chỉ dựa vào bộ đếm đúng/sai trên thẻ.
14. An toàn và tình huống lỗi
•	Không lưu mật khẩu dạng văn bản thuần và không gửi lại mật khẩu cũ qua email.
•	Không đưa khóa Gemini vào mã nguồn công khai; phương án backend trung gian hoặc tích hợp phù hợp sẽ được chốt khi triển khai. local.properties không được coi là cơ chế giữ bí mật khóa khi khóa đã đóng gói trong APK.
•	Có thông báo trước khi gửi tài liệu người dùng tới dịch vụ AI.
•	Xử lý mất mạng, timeout, hết hạn mức AI, dữ liệu không hợp lệ và lỗi lưu.
•	PDF scan hoặc file khó đọc phải có thông báo không nhận diện tốt và cho người dùng kiểm tra.
•	Nội dung đúng định dạng không đồng nghĩa đúng kiến thức; luôn duyệt phần giải thích và đáp án AI.
•	Dữ liệu tài khoản phải tách theo người dùng; nếu có đồng bộ cần quy tắc xung đột và chống tạo bản ghi trùng.
15. Trình tự triển khai đề xuất
Giai đoạn	Công việc
1	Khởi tạo Java + Kotlin DSL, minSdk 24; chốt database, điều hướng và phân công.
2	Tài khoản, Home, Storage, Profile; CRUD môn/bộ/hai loại thẻ.
3	Flashcard, Quiz phản hồi ngay và lật giải thích; lưu lịch sử/thời gian học.
4	CSV/Excel import/export; thống kê thời gian và donut kết quả.
5	Gemini tài liệu, xem trước và chat AI; lịch ôn, thông báo, Google Calendar.
6	Kiểm thử, sửa lỗi, báo cáo và chuẩn bị demo.
16. CẬP NHẬT QUYẾT ĐỊNH ĐÃ CHỐT
Phần này bổ sung và thay thế các nội dung tương ứng trước đây khi có khác biệt.
16.1. Tên ứng dụng
•	Tên ứng dụng chính thức: CardMaster.
•	Tên làm việc trước đây: FlashQuiz. Toàn bộ tài liệu, mã nguồn, tên package, giao diện và báo cáo nên thống nhất dùng CardMaster.
16.2. Định dạng và giới hạn file
•	Word: .docx
•	Excel: .xlsx
•	Giới hạn dung lượng mỗi file: 10–20 MB.
•	Quy tắc đề xuất: cho phép tối đa 20 MB; nếu file vượt 20 MB, hiển thị thông báo lỗi và không gửi xử lý.
•	Nếu muốn phân biệt mức “cảnh báo” và “tối đa”, có thể cảnh báo khi vượt 10 MB nhưng vẫn cho phép xử lý tới 20 MB.
16.3. Gemini AI
•	Model/API: sử dụng Gemini API; tên model cụ thể sẽ được cấu hình ở một nơi tập trung để dễ thay đổi khi Google cập nhật.
•	Bảo vệ khóa: không nhúng Gemini API key trực tiếp vào source code hoặc APK. Sử dụng biến môi trường/local configuration trong giai đoạn phát triển; khi triển khai thực tế, dùng backend trung gian hoặc dịch vụ quản lý khóa.
•	Hạn mức: theo dõi số request, số token và xử lý lỗi khi vượt hạn mức; hiển thị thông báo “Vượt hạn mức, thử lại sau”.
•	Thời gian lưu tài liệu: 18 ngày.
•	Sau 18 ngày, tài liệu tải lên phục vụ xử lý AI sẽ bị xóa khỏi bộ nhớ tạm/dịch vụ lưu trữ theo thiết kế.
•	Trước khi gửi tài liệu, ứng dụng hiển thị thông báo rằng nội dung sẽ được gửi tới Gemini để xử lý và được lưu tối đa 18 ngày.
16.4. Google Calendar
•	Google Calendar được đồng bộ hai chiều.
•	Ứng dụng có thể tạo, cập nhật và xóa sự kiện ôn tập trên Google Calendar.
•	Khi người dùng thay đổi lịch trong CardMaster, ứng dụng cập nhật sự kiện tương ứng trên Google Calendar.
•	Khi người dùng thay đổi/xóa sự kiện tương ứng trên Google Calendar, ứng dụng cập nhật hoặc xóa lịch ôn tập trong CardMaster.
•	Cần lưu calendarEventId để ánh xạ lịch trong app với sự kiện trên Google Calendar.
•	Cần xử lý xung đột: nếu cả app và Google Calendar thay đổi cùng một sự kiện, ưu tiên thay đổi gần nhất theo thời gian cập nhật hoặc hiển thị hộp thoại cho người dùng chọn.
•	Khi mất kết nối, lưu thay đổi cục bộ và đồng bộ lại khi có mạng.
16.5. Quiz: bỏ qua, bài chưa hoàn thành và làm lại
•	Câu bỏ qua không được tính là câu đúng và không được tính vào điểm số của bài làm trắc nghiệm.
•	Câu bỏ qua được ghi nhận riêng với trạng thái “Bỏ qua” hoặc “Skipped”.
•	Bài chưa hoàn thành: khi người dùng quay lại, ứng dụng cho phép chọn “Làm tiếp” hoặc “Làm lại từ đầu”.
•	Nếu chọn “Làm tiếp”, giữ lại các câu đã trả lời và tiếp tục từ câu chưa trả lời.
•	Nếu chọn “Làm lại từ đầu”, xóa/không sử dụng kết quả cũ của lần làm đó và bắt đầu một lần làm mới.
•	Khi làm lại bài, điểm cuối cùng của bài là điểm cao nhất giữa các lần làm.
•	Điểm trung bình được tính từ các lần làm đã hoàn thành; mỗi lần làm là một QuizAttempt riêng.
•	Công thức điểm một lần làm: Điểm (%) = (Số câu đúng / Tổng số câu được tính điểm) × 100.
•	Tổng số câu được tính điểm = tổng số câu trong bài − số câu bỏ qua.
•	Nếu người dùng bỏ qua toàn bộ câu hỏi, bài không có điểm hợp lệ và hiển thị trạng thái “Chưa hoàn thành” hoặc “Không có điểm”.
Ví dụ: bài có 10 câu, người dùng trả lời đúng 7 câu và bỏ qua 1 câu. Tổng câu được tính điểm là 9. Điểm lần làm đó là (7 / 9) × 100 = 77,78%.
Ví dụ làm lại: lần 1 đạt 70%, lần 2 đạt 85%. Điểm cuối cùng của bài là 85%. Điểm trung bình của các lần làm đã hoàn thành vẫn có thể được hiển thị riêng trong thống kê, ví dụ (70% + 85%) / 2 = 77,5%.
16.6. Tổ chức thẻ
•	Flashcard và Quiz Card có thể được lưu trong cùng một danh mục/bộ thẻ.
•	Một bộ thẻ có thể chứa cả Flashcard và Quiz Card.
•	Khi tạo thẻ, người dùng chọn loại thẻ: Flashcard hoặc Quiz Card.
•	Khi học Flashcard, chỉ xử lý các thẻ loại Flashcard.
•	Khi làm Quiz, chỉ xử lý các thẻ loại Quiz Card.
•	Chức năng “Học tất cả” có thể xử lý xen kẽ hai loại thẻ; đây là tính năng mở rộng.
•	Danh sách thẻ cần có bộ lọc theo loại: Tất cả, Flashcard, Quiz Card.
•	Chi tiết bộ thẻ hiển thị số lượng từng loại thẻ và tiến độ học tương ứng.
16.7. Cập nhật mô hình dữ liệu
•	QuizAttempt: bổ sung status (IN_PROGRESS, COMPLETED, ABANDONED), bestScore, latestScore, skippedCount, resumedFromQuestionIndex.
•	AttemptAnswer: bổ sung answerStatus (CORRECT, WRONG, SKIPPED), answeredAt, timeSpentMs.
•	StudyPlan: bổ sung calendarEventId, lastSyncedAt, syncStatus (SYNCED, PENDING_SYNC, CONFLICT, DELETED).
•	AI Document: bổ sung uploadedAt, expiresAt (uploadedAt + 18 ngày), deletionStatus, fileSizeBytes.
16.8. Cập nhật màn hình liên quan
•	Quiz Setup: thêm lựa chọn “Tiếp tục bài chưa hoàn thành” nếu tồn tại bài chưa hoàn thành của bộ thẻ đã chọn.
•	Quiz Play: thêm nút “Bỏ qua câu này”. Sau khi bỏ qua, câu được đánh dấu SKIPPED và không tính điểm.
•	Quiz Result: hiển thị tổng câu, số đúng, số sai, số bỏ qua, điểm lần làm, điểm cao nhất và tùy chọn làm lại.
•	Study Planner: hiển thị trạng thái đồng bộ Calendar: Đã đồng bộ, Chờ đồng bộ, Xung đột, Đã xóa.
•	AI Document Import: hiển thị cảnh báo về việc gửi tài liệu tới Gemini và thời gian lưu tối đa 18 ngày.
16.9. Các mục còn cần chốt
•	compileSdk, targetSdk, phiên bản Gradle/AGP/JDK và thư viện tương thích với minSdk 24.
•	Nguồn dữ liệu chính: Room, Firebase, hoặc Room offline kết hợp Firebase.
•	Cách lưu Flashcard và Quiz Card: một bảng Cards có trường type, hoặc hai bảng riêng.
•	Model Gemini cụ thể sẽ dùng trong giai đoạn phát triển và demo.
•	Cơ chế xác thực: Firebase Authentication, đăng nhập cục bộ, hay cả hai.
•	Chi tiết quyền và scope OAuth cho Google Calendar.
•	Cách xử lý xung đột đồng bộ Calendar khi thay đổi xảy ra ở cả hai phía.
17. Tài liệu bàn giao
•	Bàn giao dự kiến: source code, APK, README, ERD, sơ đồ luồng, báo cáo và file dữ liệu mẫu để demo.
•	Thiết kế màu/font và quy chuẩn hình ảnh nằm ở DESIGN.md riêng, không nằm trong tài liệu này.

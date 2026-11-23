# FlashQuiz — DESIGN.md

Tài liệu thiết kế đồng bộ cho ứng dụng Android **FlashQuiz**.  
Mục tiêu: thống nhất màu sắc, typography, spacing, bo góc, icon, trạng thái và biểu đồ thống kê trong toàn bộ app.

## 1. Nguyên tắc thiết kế

- Giao diện tối giản, tập trung vào nội dung học tập.
- Mỗi màn hình chỉ có một hành động chính rõ ràng.
- Dùng màu để phân biệt trạng thái học tập, không dùng màu quá nhiều cho trang trí.
- Flashcard và Quiz Card phải dễ phân biệt bằng icon và màu nhấn.
- Mọi trạng thái trống phải có icon, tiêu đề, mô tả và nút hành động.
- Bottom Navigation chỉ chứa 5 đích chính: Home, Thống kê, Chat AI, Storage, Profile.

## 2. Bảng màu

### Màu chính

| Token | Mã màu | Dùng cho |
|---|---:|---|
| `primary` | `#4F46E5` | Nút chính, tab đang chọn, link, progress chính |
| `primary_container` | `#E0E7FF` | Nền nhẹ cho thành phần chính |
| `on_primary` | `#FFFFFF` | Chữ/icon trên màu primary |
| `on_primary_container` | `#1E1B4B` | Chữ trên primary container |
| `secondary` | `#0EA5E9` | Hành động phụ, AI, biểu đồ phụ |
| `secondary_container` | `#E0F2FE` | Nền nhấn phụ |
| `on_secondary` | `#FFFFFF` | Chữ/icon trên secondary |
| `on_secondary_container` | `#0C4A6E` | Chữ trên secondary container |
| `accent` | `#F59E0B` | Điểm nhấn, streak, thành tựu, cảnh báo nhẹ |
| `accent_container` | `#FEF3C7` | Nền nhấn vàng nhạt |

### Màu nền và chữ

| Token | Mã màu | Dùng cho |
|---|---:|---|
| `background` | `#F8FAFC` | Nền màn hình sáng |
| `surface` | `#FFFFFF` | Card, dialog, bottom sheet |
| `surface_variant` | `#EEF2F7` | Input, chip, vùng phụ |
| `outline` | `#CBD5E1` | Viền input, divider |
| `text_primary` | `#0F172A` | Tiêu đề, nội dung chính |
| `text_secondary` | `#475569` | Mô tả, label phụ |
| `text_disabled` | `#94A3B8` | Chữ vô hiệu |
| `divider` | `#E2E8F0` | Đường phân cách |

### Màu trạng thái học tập

| Token | Mã màu | Ý nghĩa |
|---|---:|---|
| `success` | `#16A34A` | Đã nhớ, đáp án đúng, hoàn thành |
| `success_container` | `#DCFCE7` | Nền trạng thái thành công |
| `on_success_container` | `#14532D` | Chữ trên nền thành công |
| `warning` | `#F59E0B` | Cần ôn lại, tiến độ trung bình |
| `warning_container` | `#FEF3C7` | Nền cảnh báo |
| `on_warning_container` | `#78350F` | Chữ trên nền cảnh báo |
| `error` | `#DC2626` | Đáp án sai, lỗi, xóa |
| `error_container` | `#FEE2E2` | Nền lỗi |
| `on_error_container` | `#7F1D1D` | Chữ trên nền lỗi |
| `info` | `#0EA5E9` | Thông tin, AI, đồng bộ Calendar |
| `info_container` | `#E0F2FE` | Nền thông tin |

### Màu loại thẻ

| Token | Mã màu | Loại thẻ |
|---|---:|---|
| `flashcard_color` | `#4F46E5` | Flashcard ôn tập |
| `flashcard_container` | `#E0E7FF` | Nền flashcard |
| `quiz_color` | `#0EA5E9` | Quiz Card |
| `quiz_container` | `#E0F2FE` | Nền quiz card |

### Dark mode

| Token | Mã màu |
|---|---:|
| `background_dark` | `#0B1120` |
| `surface_dark` | `#111827` |
| `surface_variant_dark` | `#1F2937` |
| `outline_dark` | `#334155` |
| `text_primary_dark` | `#F8FAFC` |
| `text_secondary_dark` | `#CBD5E1` |
| `primary_dark` | `#818CF8` |
| `secondary_dark` | `#38BDF8` |
| `success_dark` | `#4ADE80` |
| `warning_dark` | `#FBBF24` |
| `error_dark` | `#F87171` |

## 3. Typography

Dùng font **Inter** cho toàn bộ ứng dụng.  
Nếu muốn hỗ trợ tiếng Việt tốt và nhẹ hơn, có thể dùng **Be Vietnam Pro** làm font chính.

| Token | Font | Weight | Size | Line height | Dùng cho |
|---|---|---:|---:|---:|---|
| `display_large` | Inter / Be Vietnam Pro | Bold | 32sp | 40sp | Điểm số lớn, kết quả quiz |
| `headline_large` | Inter / Be Vietnam Pro | Bold | 28sp | 36sp | Tiêu đề màn hình lớn |
| `headline_medium` | Inter / Be Vietnam Pro | SemiBold | 24sp | 32sp | Tiêu đề section |
| `title_large` | Inter / Be Vietnam Pro | SemiBold | 20sp | 28sp | Tên bộ thẻ, tiêu đề card |
| `title_medium` | Inter / Be Vietnam Pro | SemiBold | 16sp | 24sp | Tiêu đề item, nút quan trọng |
| `title_small` | Inter / Be Vietnam Pro | Medium | 14sp | 20sp | Nhãn nhỏ, tab |
| `body_large` | Inter / Be Vietnam Pro | Regular | 16sp | 24sp | Nội dung chính, câu hỏi |
| `body_medium` | Inter / Be Vietnam Pro | Regular | 14sp | 20sp | Mô tả, nội dung phụ |
| `body_small` | Inter / Be Vietnam Pro | Regular | 12sp | 16sp | Timestamp, helper text |
| `label_large` | Inter / Be Vietnam Pro | Medium | 14sp | 20sp | Nút, chip |
| `label_medium` | Inter / Be Vietnam Pro | Medium | 12sp | 16sp | Badge, tag |
| `label_small` | Inter / Be Vietnam Pro | Medium | 11sp | 16sp | Badge nhỏ |

### Quy tắc chữ

- Tiêu đề màn hình dùng `title_large`.
- Nội dung flashcard dùng `headline_medium` hoặc `title_large` tùy độ dài.
- Nội dung quiz dùng `body_large` để dễ đọc.
- Đáp án dùng `body_medium`.
- Giải thích sau khi lật thẻ dùng `body_medium`, màu `text_secondary`.
- Điểm số quiz dùng `display_large`.
- Không dùng quá 3 level chữ trong một màn hình.

## 4. Spacing và layout

| Token | Giá trị | Dùng cho |
|---|---:|---|
| `space_4` | 4dp | Khoảng cách rất nhỏ |
| `space_8` | 8dp | Khoảng cách nhỏ |
| `space_12` | 12dp | Khoảng cách giữa các phần tử liên quan |
| `space_16` | 16dp | Padding màn hình, khoảng cách chuẩn |
| `space_20` | 20dp | Khoảng cách giữa section |
| `space_24` | 24dp | Khoảng cách lớn |
| `space_32` | 32dp | Khoảng cách rất lớn |
| `screen_padding` | 16dp | Padding trái/phải mặc định |
| `card_padding` | 16dp | Padding trong card |
| `button_height` | 52dp | Chiều cao nút chính |
| `input_height` | 56dp | Chiều cao input |
| `bottom_nav_height` | 72dp | Chiều cao bottom navigation |
| `top_app_bar_height` | 64dp | Chiều cao top app bar |
| `flashcard_min_height` | 320dp | Chiều cao tối thiểu flashcard |
| `quiz_option_height` | 56dp | Chiều cao đáp án |

## 5. Bo góc, border, elevation

| Thành phần | Corner radius | Border | Elevation |
|---|---:|---:|---:|
| Button | 16dp | Không | 0dp |
| Input | 12dp | 1dp `outline` | 0dp |
| Card | 16dp | Không | 1dp |
| Flashcard | 24dp | Không | 2dp |
| Quiz option | 12dp | 1dp `outline` | 0dp |
| Chip | 999dp | Không | 0dp |
| Bottom sheet | 24dp trên | Không | 8dp |
| Dialog | 24dp | Không | 8dp |
| FAB | 16dp | Không | 3dp |
| Avatar | 999dp | Không | 0dp |

## 6. Button

### Primary Button

- Background: `primary`.
- Text: `on_primary`.
- Corner radius: 16dp.
- Height: 52dp.
- Font: `title_medium`.
- Dùng cho: Đăng nhập, Đăng ký, Lưu, Bắt đầu học, Bắt đầu quiz, Tạo nội dung bằng AI.

### Secondary Button

- Background: `secondary_container`.
- Text: `on_secondary_container`.
- Border: không.
- Dùng cho: Học lại, Làm quiz, Xem chi tiết, Đồng bộ Calendar.

### Outlined Button

- Background: trong suốt.
- Border: 1dp `outline`.
- Text: `text_primary`.
- Dùng cho: Hủy, Quay lại, Chọn file, Xuất file.

### Danger Button

- Background: `error`.
- Text: `#FFFFFF`.
- Dùng cho: Xóa bộ thẻ, Xóa thẻ, Xóa lịch, Đăng xuất.

### Disabled Button

- Background: `surface_variant`.
- Text: `text_disabled`.
- Không cho phép bấm.

## 7. Input

- Background: `surface`.
- Border: 1dp `outline`.
- Corner radius: 12dp.
- Height: 56dp.
- Label: `label_large`, màu `text_secondary`.
- Nội dung nhập: `body_large`, màu `text_primary`.
- Helper text: `body_small`, màu `text_secondary`.
- Error text: `body_small`, màu `error`.
- Icon cuối cho mật khẩu: eye / eye-off.
- Trạng thái focus: border 2dp `primary`.

## 8. Card và list item

### Card cơ bản

- Background: `surface`.
- Corner radius: 16dp.
- Padding: 16dp.
- Elevation: 1dp.
- Title: `title_medium`.
- Description: `body_medium`, màu `text_secondary`.

### Deck Card

```text
┌──────────────────────────────────────────┐
│ [icon]  Room & SQLite                    │
│         Lập trình di động                │
│                                          │
│ 12/20 thẻ đã nhớ                         │
│ ██████████░░░░░░░░░░ 60%                 │
│                                          │
│ [Học]  [Quiz]  [⋮]                       │
└──────────────────────────────────────────┘
```

### Flashcard Item

- Icon: `style` hoặc `flip`.
- Màu icon: `flashcard_color`.
- Hiển thị mặt trước và mặt sau thu gọn.
- Badge trạng thái: Mới / Đã nhớ / Cần ôn lại.

### Quiz Card Item

- Icon: `quiz`.
- Màu icon: `quiz_color`.
- Hiển thị câu hỏi và số đáp án.
- Badge: Đã làm / Chưa làm / Đúng / Sai.

## 9. Flashcard Study

### Flashcard

- Background: `surface`.
- Corner radius: 24dp.
- Padding: 24dp.
- Minimum height: 320dp.
- Mặt trước:
  - Label nhỏ: “Câu hỏi”.
  - Nội dung: `headline_medium`.
- Mặt sau:
  - Label nhỏ: “Đáp án”.
  - Nội dung: `body_large`.
- Nút lật thẻ:
  - Text: “Lật thẻ”.
  - Icon: `flip`.
  - Style: secondary button.

### Hành động sau khi lật

| Nút | Màu nền | Màu chữ | Icon |
|---|---|---|---|
| Chưa nhớ | `error_container` | `on_error_container` | `close` |
| Đã nhớ | `success_container` | `on_success_container` | `check` |

## 10. Quiz Card

### Trước khi trả lời

- Câu hỏi: `body_large`.
- Đáp án: card 56dp, corner radius 12dp.
- Border: 1dp `outline`.
- Background: `surface`.
- Text: `text_primary`.
- Prefix: A / B / C / D.

### Sau khi chọn

| Trạng thái | Background | Border | Text |
|---|---|---|---|
| Chưa chọn | `surface` | `outline` | `text_primary` |
| Đã chọn | `primary_container` | `primary` | `on_primary_container` |
| Chọn đúng | `success_container` | `success` | `on_success_container` |
| Chọn sai | `error_container` | `error` | `on_error_container` |
| Đáp án đúng | `success_container` | `success` | `on_success_container` |

### Thẻ giải thích

Sau khi chọn đáp án, hiển thị nút **Lật thẻ để xem giải thích**.

```text
┌──────────────────────────────────────────┐
│ KẾT QUẢ                                  │
│ Sai ✗                                    │
│                                          │
│ Bạn chọn: A. Quản lý giao diện           │
│ Đáp án đúng: B. Lưu dữ liệu SQLite       │
│                                          │
│ GIẢI THÍCH                               │
│ Room là persistence library cung cấp     │
│ lớp trừu tượng trên SQLite.              │
│                                          │
│ [Lật thẻ để xem giải thích]              │
└──────────────────────────────────────────┘
```

Sau khi lật:

```text
┌──────────────────────────────────────────┐
│ GIẢI THÍCH                               │
│ Room không dùng để quản lý UI.           │
│ Room là thư viện lưu trữ và truy vấn     │
│ dữ liệu SQLite trong Android.            │
│                                          │
│ Nguồn: Trang 12 — Tài liệu Room.pdf      │
│                                          │
│ [Câu tiếp theo]                          │
└──────────────────────────────────────────┘
```

## 11. Thống kê

Màn hình Statistics gồm hai khối chính:

1. **Thống kê thời gian học tập**.
2. **Thống kê bài tập đã làm**.

### Bộ lọc thời gian

Dùng filter chips:

```text
[ Hôm nay ] [ 7 ngày ] [ 30 ngày ] [ Tất cả ]
```

- Chip đang chọn: background `primary`, text `on_primary`.
- Chip chưa chọn: background `surface_variant`, text `text_secondary`.

### Thống kê thời gian học

Hiển thị:

- Tổng thời gian học.
- Thời gian học trung bình mỗi phiên.
- Số phiên học.
- Số thẻ đã học.
- Biểu đồ cột thời gian học theo ngày.

```text
┌──────────────────────────────────────────┐
│ THỜI GIAN HỌC TẬP                        │
│                                          │
│ 4 giờ 25 phút                            │
│ Trung bình 32 phút/phiên                 │
│ 8 phiên học                              │
│                                          │
│ [Biểu đồ cột theo ngày]                  │
└──────────────────────────────────────────┘
```

### Biểu đồ cột thời gian học

- Màu cột: `primary`.
- Màu cột ngày hiện tại: `secondary`.
- Trục X: ngày.
- Trục Y: phút học.
- Corner radius cột: 6dp.
- Grid line: `divider`.
- Label: `label_small`.

### Thống kê bài tập đã làm

Dùng **biểu đồ tròn / donut chart** để thể hiện kết quả bài tập.

```text
┌──────────────────────────────────────────┐
│ BÀI TẬP ĐÃ LÀM                           │
│                                          │
│        ┌───────────┐                     │
│        │   80%     │                     │
│        │  Đúng     │                     │
│        └───────────┘                     │
│                                          │
│ ● Đúng: 32 câu                           │
│ ● Sai: 8 câu                             │
│                                          │
│ 5 bài quiz đã hoàn thành                 │
│ Điểm trung bình: 80%                     │
└──────────────────────────────────────────┘
```

### Màu biểu đồ tròn

| Phần | Màu | Ý nghĩa |
|---|---:|---|
| Đúng | `success` | Số câu trả lời đúng |
| Sai | `error` | Số câu trả lời sai |
| Chưa trả lời | `surface_variant` | Câu bỏ qua hoặc chưa hoàn thành |

### Quy tắc biểu đồ tròn

- Dùng donut chart, không dùng pie chart đặc.
- Độ dày vòng: 16dp.
- Kích thước: 160dp x 160dp.
- Ở giữa hiển thị:
  - Phần trăm đúng.
  - Nhãn “Đúng”.
- Legend bên dưới hoặc bên phải.
- Nếu chưa có dữ liệu, hiển thị empty state.

### Empty state thống kê

```text
┌──────────────────────────────────────────┐
│ [icon: bar_chart]                        │
│                                          │
│ Chưa có dữ liệu thống kê                 │
│ Hãy học flashcard hoặc làm quiz          │
│ để xem tiến độ của bạn.                  │
│                                          │
│ [Bắt đầu học]                            │
└──────────────────────────────────────────┘
```

## 12. Icon

Dùng Material Symbols Outlined.

| Chức năng | Icon |
|---|---|
| Home | `home` |
| Thống kê | `bar_chart` |
| Chat AI | `smart_toy` |
| Storage | `folder` |
| Profile | `person` |
| Thông báo | `notifications` |
| Thêm | `add` |
| Tìm kiếm | `search` |
| Back | `arrow_back` |
| Flashcard | `style` |
| Quiz | `quiz` |
| Lật thẻ | `flip` |
| Đã nhớ | `check_circle` |
| Chưa nhớ | `cancel` |
| Import | `upload_file` |
| Export | `download` |
| PDF/Word | `description` |
| AI | `auto_awesome` |
| Calendar | `event` |
| Cài đặt | `settings` |
| Đăng xuất | `logout` |
| Xóa | `delete` |
| Sửa | `edit` |

## 13. Component trạng thái

### Badge

| Trạng thái | Background | Text |
|---|---|---|
| Mới | `surface_variant` | `text_secondary` |
| Đã nhớ | `success_container` | `on_success_container` |
| Cần ôn lại | `warning_container` | `on_warning_container` |
| Đúng | `success_container` | `on_success_container` |
| Sai | `error_container` | `on_error_container` |
| AI generated | `secondary_container` | `on_secondary_container` |

### Progress bar

- Height: 8dp.
- Corner radius: 999dp.
- Background: `surface_variant`.
- Progress: `primary`.
- Progress của quiz: `secondary`.
- Progress của thẻ cần ôn lại: `warning`.

### Loading

- Dùng `CircularProgressIndicator` màu `primary`.
- Text trạng thái:
  - “Đang tải…”
  - “Đang đọc tài liệu…”
  - “Gemini đang tạo nội dung…”
  - “Đang lưu dữ liệu…”

## 14. Empty state

Mỗi empty state gồm:

- Icon lớn.
- Tiêu đề.
- Mô tả ngắn.
- Nút hành động chính.

| Màn hình | Tiêu đề | Mô tả | Nút |
|---|---|---|---|
| Home | Chưa có bộ thẻ | Tạo bộ thẻ đầu tiên để bắt đầu học | Tạo bộ thẻ |
| Notifications | Chưa có thông báo | Nhắc nhở và kết quả học sẽ xuất hiện tại đây | Về trang chủ |
| Statistics | Chưa có dữ liệu | Học hoặc làm quiz để xem thống kê | Bắt đầu học |
| Storage | Kho nội dung trống | Tạo môn học để tổ chức bộ thẻ | Tạo môn học |
| Deck List | Chưa có bộ thẻ | Thêm bộ thẻ cho môn học này | Tạo bộ thẻ |
| Card List | Chưa có thẻ | Thêm thẻ hoặc nhập từ file | Thêm thẻ |
| AI Preview | Không có nội dung phù hợp | Thử tài liệu khác hoặc điều chỉnh yêu cầu | Thử lại |
| Search | Không tìm thấy kết quả | Kiểm tra lại từ khóa tìm kiếm | Xóa tìm kiếm |

## 15. Animation

| Thành phần | Animation | Thời lượng |
|---|---|---:|
| Lật flashcard | `flipHorizontally` | 250ms |
| Chuyển câu quiz | fade + slide nhẹ | 200ms |
| Hiện kết quả đúng/sai | scale + fade | 200ms |
| Progress bar | animateFloatAsState | 300ms |
| Bottom sheet | slide từ dưới lên | 250ms |
| Loading AI | shimmer hoặc circular progress | Theo trạng thái |

## 16. Quy tắc đặt tên

### Color token

```text
color_primary
color_primary_container
color_on_primary
color_surface
color_background
color_text_primary
color_text_secondary
color_success
color_error
color_warning
color_info
color_flashcard
color_quiz
```

### Typography token

```text
type_display_large
type_headline_medium
type_title_large
type_title_medium
type_body_large
type_body_medium
type_body_small
type_label_large
type_label_medium
```

### Spacing token

```text
space_4
space_8
space_12
space_16
space_20
space_24
space_32
```

### Component token

```text
component_button_primary
component_button_secondary
component_button_outlined
component_button_danger
component_input
component_card
component_flashcard
component_quiz_option
component_badge
component_progress_bar
component_empty_state
```

## 17. Checklist trước khi build UI

- [ ] Mọi màn hình dùng đúng `screen_padding = 16dp`.
- [ ] Mọi nút chính dùng `primary`.
- [ ] Mọi input có label, helper text và error state.
- [ ] Flashcard và Quiz Card có icon và màu riêng.
- [ ] Quiz Card luôn cho lật xem giải thích, kể cả khi chọn sai.
- [ ] Thống kê có biểu đồ cột thời gian học và donut chart kết quả quiz.
- [ ] Mọi danh sách có empty state.
- [ ] Mọi màn hình con có nút back.
- [ ] Bottom navigation chỉ có 5 mục.
- [ ] Dark mode dùng đúng token màu dark.

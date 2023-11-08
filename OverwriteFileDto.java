package lynorg.filemngt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OverwriteFileDto {
    private String rootFolderPath;
    private String originalString;
    private String replaceString;
}

package practice.itemservice.web.form;

import lombok.extern.slf4j.Slf4j;
import practice.itemservice.domain.item.DeliveryCode;
import practice.itemservice.domain.item.Item;
import practice.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import practice.itemservice.domain.item.ItemType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    @ModelAttribute("region")
    public Map<String,String>regions(){
        Map<String,String>regions=new LinkedHashMap<>();
        regions.put("SEOUL","서울");
        regions.put("BUSAN","부산");
        regions.put("JEJU","제주");
        return regions;
    }
    // 컨트롤러 호출시 @ModelAttribute가 적용된 메서드에서 반환한 값을 자동으로 모델에 담는다.

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes(){
        return ItemType.values(); //ENUM의 모든 정보를 배열로 반환한다.
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        List<DeliveryCode> deliveryCodes=new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST","퀵 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL","일반 배송"));
        return deliveryCodes;
    }
    // ENUM으로도 여러개의 선택지 중 하나를 선택하도록 할 수 있지만,

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item",new Item());
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}",item.getOpen());
        log.info("item.regions={}",item.getRegions());
        log.info("item.itemType={}",item.getItemType());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}


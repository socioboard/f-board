//
//  ProfileViewController.m
//  SwitchUserSample
//
//  Created by GBS-ios on 4/25/15.
//
//

#import "ProfileViewController.h"
#import "SUCache.h"
#import "SingletonClass.h"
#import "SingletonClass.h"
#import "TableCustomCell.h"
#import <QuartzCore/QuartzCore.h>
#import "ImageViewCustomCell.h"


@interface ProfileViewController ()

@end

@implementation ProfileViewController


-(void)viewWillAppear:(BOOL)animated{
   self.navigationController.navigationBarHidden=YES;
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(reloadDetails:) name:@"CurrentUserChangedNotification" object:nil];
    
    [self fetchMyDetails];
    
    [self fetchMyProfileDetails];

}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.scrollView=[[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    self.scrollView.delegate=self;
    self.scrollView.backgroundColor=[UIColor clearColor];
    self.scrollView.contentSize=CGSizeMake(0,self.view.frame.size.height+200);
//    self.scrollView.contentOffset=CGPointMake(0,self.view.frame.size.height+200);
    [self.view addSubview:self.scrollView];
    
    self.photoIdArray = [[NSMutableArray alloc]init];
    self.pictureArray = [[NSMutableArray alloc]init];
    
    self.profileView = [[UIView alloc]init];
    self.profileView.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height/3-20);
    self.profileView.backgroundColor = [UIColor whiteColor];
    [self.scrollView addSubview:self.profileView];
    
    self.coverImageview=[[UIImageView alloc]init];
    self.coverImageview.userInteractionEnabled = YES;
        self.coverImageview.frame=CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height/3-20);
    [self.profileView addSubview:self.coverImageview];
    
    self.profileImageView=[[FBSDKProfilePictureView alloc]init];
    self.profileImageView.userInteractionEnabled = YES;
    // self.profileImageView.image = [UIImage imageNamed:@"hel.jpeg"];
    self.profileImageView.frame=CGRectMake(30, self.view.frame.size.height/8, 80, 80);
    
    [self.profileImageView.layer setBorderColor: [[UIColor blackColor] CGColor]];
    [self.profileImageView.layer setBorderWidth: 2.0];
//    self.profileImageView.layer.cornerRadius=20;
//    self.profileImageView.layer.masksToBounds = YES;
    [self.profileView addSubview:self.profileImageView];
    
    self.nameLabel = [[UILabel alloc]init];
//    self.nameLabel.text = @"Vinayaka";
    self.nameLabel.numberOfLines = 1;
    self.nameLabel.font=[UIFont fontWithName:@"Helvetica-Bold" size:15];
    self.nameLabel.frame = CGRectMake(120,self.view.frame.size.height/8, 200, 30);
    [self.profileView addSubview:self.nameLabel];
    
    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
    
    self.view.backgroundColor=[UIColor colorWithRed:250.0f/255.0f green:250.0f/255.0f blue:250.f alpha:0.8f];
    [self createFollowTable];
    [self createCollectionView];
    
       // Do any additional setup after loading the view.
}

-(void)reloadDetails:(NSNotificationCenter *)defaultCenter{
    
    
    [self fetchMyDetails];
    
    [self fetchMyProfileDetails];


}

-(void)fetchMyProfileDetails{
    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:indexPath.row+indexPath.section].token;
    NSString *str=[NSString stringWithFormat:@"/%@?fields=id,name,work,location,hometown,albums,birthday,cover",@"me"];
    if (token) {
        

        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:str parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            NSLog(@"result is %@",result);
            
            self.locationString =[[result objectForKey:@"location"]objectForKey:@"name"];
            self.coverString = [[result objectForKey:@"cover"]objectForKey:@"source"];
            NSLog(@"self.coverstring %@",self.coverString);
            
            [self.coverImageview setImageWithURL:[NSURL URLWithString:self.coverString] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
//            [self.profileImageView setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/me/picture?type=small"]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
            self.profileImageView.profileID=[FBSDKAccessToken currentAccessToken].userID;
            
             for (NSDictionary *dic in [result objectForKey:@"work"] ) {
                  self.workString = [[dic objectForKey:@"employer"]objectForKey:@"name"];
             }

           
            self.homeTownString =[[result objectForKey:@"hometown"]objectForKey:@"name"];
            
            self.nameString =[result objectForKey:@"name"];
            self.nameLabel.text=self.nameString;
            
            self.birthdayString = [result objectForKey:@"birthday"];
            
            [profileTableView reloadData];
        }];
        [self getUrlOfPhotos];
    }

}

-(void)createCollectionView{
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout setItemSize:CGSizeMake(90, 100)];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    CGSize s =[UIScreen mainScreen].bounds.size;
    if (!photoCollectionView) {

    if([UIScreen mainScreen].bounds.size.height>500)
    {
        photoCollectionView=[[UICollectionView alloc] initWithFrame:CGRectMake(10,profileTableView.frame.size.height+profileTableView.frame.origin.y-50, s.width-20,self.scrollView.contentSize.height-2*self.view.frame.size.height/3-50 ) collectionViewLayout:flowLayout];
    }
    else{
        photoCollectionView=[[UICollectionView alloc] initWithFrame:CGRectMake(10, profileTableView.frame.size.height+profileTableView.frame.origin.y-50, s.width-20,self.scrollView.contentSize.height-2*self.view.frame.size.height/3-50) collectionViewLayout:flowLayout];
    }
        
        if (IS_IPHONE_6P) {
            photoCollectionView=[[UICollectionView alloc] initWithFrame:CGRectMake(10,profileTableView.frame.size.height+profileTableView.frame.origin.y-50, s.width-20, self.scrollView.contentSize.height-2*self.view.frame.size.height/3+80) collectionViewLayout:flowLayout];

        }
    
    [photoCollectionView setDataSource:self];
    [photoCollectionView setDelegate:self];
    [photoCollectionView registerClass:[ImageViewCustomCell class] forCellWithReuseIdentifier:@"cell"];
    //  [filtercollectioView setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"background.png"]]];
    [photoCollectionView setBackgroundColor:[UIColor whiteColor]];
    [photoCollectionView setCollectionViewLayout:flowLayout];
    [self.scrollView addSubview:photoCollectionView];
    }else{
        [photoCollectionView reloadData];
    }
}


#pragma mark collectionview delegates
#pragma mark ----------

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    
    return self.pictureArray.count;
  
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    NSString * identifier=@"cell";
    ImageViewCustomCell * cell=(ImageViewCustomCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
    NSString *str = [self.pictureArray objectAtIndex:indexPath.row];
    cell.photoImgView.hidden=NO;
    [cell.photoImgView setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    
    cell.layer.cornerRadius=4.0;
    cell.layer.borderWidth=0.5;
    cell.layer.borderColor=[[UIColor blackColor] CGColor];
    cell.textLabel.textColor=[UIColor blackColor];
    
    cell.layer.shadowColor = [UIColor blackColor].CGColor;
    cell.layer.shadowOpacity = 0.4f;
    cell.layer.shadowOffset = CGSizeMake(0.0f, 4.0f);
    cell.layer.shadowRadius = 1.5f;
    cell.layer.masksToBounds = NO;

          return cell;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    
    NSLog(@"index path of row %ld",(long)indexPath.row);
    if (!self.imageView) {
         self.imageView=[[UIImageView alloc] initWithFrame:CGRectMake(40, 100, self.view.frame.size.width-80, self.view.frame.size.height-300)];
        self.imageView.layer.borderColor=[[UIColor grayColor] CGColor];
        self.imageView.layer.borderWidth=2.0;
        self.imageView.layer.cornerRadius=2.0;
        self.imageView.userInteractionEnabled=YES;
       [self.view addSubview:self.imageView];
        
        self.cancelButton=[UIButton buttonWithType:UIButtonTypeCustom];
        [self.cancelButton addTarget:self action:@selector(cancelButtonTap:) forControlEvents:UIControlEventTouchUpInside];
        self.cancelButton.frame=CGRectMake(self.imageView.frame.size.width-30, 0, 30, 30);
        [self.cancelButton setImage:[UIImage imageNamed:@"close.png"] forState:UIControlStateNormal];
        [self.imageView addSubview:self.cancelButton];
    }else{
        self.imageView.hidden=NO;
    }
   
    [self.imageView setImageWithURL:[NSURL URLWithString:[self.pictureArray objectAtIndex:indexPath.row]] placeholderImage:[UIImage imageNamed:@""]];
   
//        [self.view bringSubviewToFront:self.imageView];
    
}

-(void)cancelButtonTap:(UIButton *)button{
    self.imageView.hidden=YES;

}

-(void)createFollowTable
{
    
    if (IS_IPHONE_6P) {
        profileTableView=[[UITableView alloc]initWithFrame:CGRectMake(10,self.view.frame.size.height/3, self.view.frame.size.width-20,self.view.frame.size.height/3) style:UITableViewStylePlain];

    }else if (IS_IPHONE_4_OR_LESS){
      profileTableView=[[UITableView alloc]initWithFrame:CGRectMake(10,self.view.frame.size.height/3, self.view.frame.size.width-20,self.view.frame.size.height/3+80) style:UITableViewStylePlain];
    
    }
    
    else{
    profileTableView=[[UITableView alloc]initWithFrame:CGRectMake(10,self.view.frame.size.height/3, self.view.frame.size.width-20,self.view.frame.size.height/3+50) style:UITableViewStylePlain];
    }
        profileTableView.dataSource=self;
        profileTableView.delegate=self;
       profileTableView.scrollEnabled=NO;
        profileTableView.backgroundColor=[UIColor colorWithRed:225.0f/255.0f green:225.0f/255.0f blue:225.0f/255.0f alpha:0.1f];
    
//    profileTableView.layer.shadowColor = [UIColor blackColor].CGColor;
//    profileTableView.layer.shadowOpacity = 0.6f;
//    profileTableView.layer.shadowOffset = CGSizeMake(0.0f, 10.0f);
//    profileTableView.layer.shadowRadius = 2.0f;
//    profileTableView.layer.masksToBounds = NO;

        [self.scrollView addSubview:profileTableView];
    
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
    
}


-(void)fetchMyDetails{
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    

    self.pictureArray=[[NSMutableArray alloc]init];
    self.photoIdArray=[[NSMutableArray alloc] init];
    
    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
   FBSDKAccessToken *token = [SUCache itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        NSString *str=[NSString stringWithFormat:@"/%@/albums",@"me"];
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:str parameters:nil];
      
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//            NSLog(@"result is %@",result);
            
            NSString *str1 = [result objectForKey:@"id"];
            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"id"];
                
                if (str1) {
                    [self.photoIdArray addObject:str1];
                }

            }

            [self getUrlOfPhotos];
        }];
   }
    
}


-(void)getUrlOfPhotos{
   NSIndexPath *indexPath= [SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        for (int i=0; i<self.photoIdArray.count; i++) {
            
//        if (self.photoIdArray.count>1) {
          
        NSString *str = [self.photoIdArray objectAtIndex:i];
        
        NSString *checkStr = [NSString stringWithFormat:@"%@/photos",str];
        
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:checkStr parameters:nil];
        
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//            NSLog(@"result is======== %@",result);
            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"picture"];
                
                if (str1) {
                    [self.pictureArray addObject:str1];
                }
                
            }
//            NSLog(@"photo id array is %lu & array is ",(unsigned long)self.pictureArray.count);
//            NSLog(@"photo id array is %@ ",self.pictureArray);
            
            [photoCollectionView reloadData];
           
        }];
    
    }
    }

}

#pragma mark tableview delegates
#pragma mark ----------

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display";
    
     UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                       reuseIdentifier:CellIdentifier] ;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    cell.textLabel.font = [UIFont fontWithName:@"Menlo-BoldItalic" size:12];
    if (indexPath.section ==0) {
        cell.textLabel.text = [NSString stringWithFormat:@"Works at %@",self.workString];
        
        cell.imageView.image = [UIImage imageNamed:@"briefcase.png"];
    }
    
    if (indexPath.section ==1) {
        cell.textLabel.text =  [NSString stringWithFormat:@"Lives at %@",self.locationString];
        cell.imageView.image = [UIImage imageNamed:@"place.png"];
   }
    
    if (indexPath.section ==2) {
        cell.textLabel.text =   [NSString stringWithFormat:@"Hometown  %@",self.homeTownString];
        cell.imageView.image = [UIImage imageNamed:@"home.png"];
  }
    
    if (indexPath.section ==3) {
        cell.textLabel.text = [NSString stringWithFormat:@"Birthday  %@",self.birthdayString];
        cell.imageView.image = [UIImage imageNamed:@"birthday.png"];

    }
    
    cell.backgroundColor=[UIColor whiteColor];
    cell.textLabel.numberOfLines=0;
    cell.textLabel.lineBreakMode=NSLineBreakByWordWrapping;
    
    [cell.textLabel sizeToFit];
    cell.layer.cornerRadius=5.0;
    cell.layer.borderWidth=0.5;
    cell.layer.borderColor=[[UIColor brownColor] CGColor];
    cell.textLabel.textColor=[UIColor blackColor];
    
//    cell.layer.shadowColor = [UIColor blackColor].CGColor;
//    cell.layer.shadowOpacity = 0.8f;
//    cell.layer.shadowOffset = CGSizeMake(0.0f, 10.0f);
//    cell.layer.shadowRadius = 3.5f;
//    cell.layer.masksToBounds = NO;

    
    return cell;
    
}


-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    
}

-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return 4;
}

-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 35;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 5;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 5;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
